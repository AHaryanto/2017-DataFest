library(XML)
library(ggplot2)
library(caret)
library(quanteda)
library(e1071)
library(irlba)
library(stringr)
library(lsa)

setwd("~/Documents/Constitution")
files <-
  list.files("~/Documents/Constitution")

# Function to extract constitution text from each file
constitution <- function(html) {
  doc.html <- htmlTreeParse(html, useInternal = TRUE)
  doc.text <- unlist(xpathApply(doc.html, '//p', xmlValue))
  doc.text <- gsub('\\n', ' ', doc.text)
  doc.text <- paste(doc.text, collapse = ' ')
  doc.text
}

text <- sapply(files, constitution, USE.NAMES = FALSE)

# Function to get country names from files
sep <- function(a) {
  unlist(strsplit(a, split = ".htm"))[1]
}

# Apply sep function to all files to get column of country names
countries <- tolower(sapply(files, sep, USE.NAMES = FALSE))

# Merge country names with constitution text
constitue_df <- data.frame(countries, text)


# Distribution of text lengths plotted in 10,000 word bins
nwords <- function(string, pseudo = F) {
  ifelse(pseudo,
         pattern <- "\\S+",
         pattern <- "[[:alpha:]]+")
  str_count(string, pattern)
}

constitue_df_length <- constitue_df
constitue_df_length$textlength <- nwords(constitue_df$text)

# Graph the distribution of text lengths
require(scales)
ggplot(constitue_df_length, aes(x = textlength)) +
  scale_x_continuous(labels = comma) +
  theme_bw() +
  geom_histogram(binwidth = 1000) +
  labs(y = "# of countries", x = "Words in Text",
       title = "Distribution of Text Lengths")

# Convert dataframe from factors to characters
constitue_df[] <- lapply(constitue_df, as.character)

# Tokenize Constitution texts
corpus_tokens <- tokens(
  constitue_df$text,
  what = "word",
  remove_numbers = TRUE,
  remove_punct = TRUE,
  remove_symbols = TRUE,
  remove_hyphens = TRUE
)

# Lower case the tokens.
corpus_tokens <- tokens_tolower(corpus_tokens)

# Exclude Quanteda package stopwords
# Explore english stop words by uncommenting next line
corpus_tokens <- tokens_select(corpus_tokens, stopwords(),
                               selection = "remove")

# Perform stemming on the tokens.
corpus_tokens <-
  tokens_wordstem(corpus_tokens, language = "english")

# Create first bag-of-words model.
corpus_tokens.dfm <- dfm(corpus_tokens, tolower = FALSE)

# Add bigrams to our feature matrix.
corpus_tokens <- tokens_ngrams(corpus_tokens, n = 1:2)
typeof(corpus_tokens)

# Transform to dfm and then a matrix.
corpus_tokens.dfm <- dfm(corpus_tokens, tolower = FALSE)
rownames(corpus_tokens.dfm) <- countries

# Weight a dfm by term frequency-inverse document frequency
corpus_tokens.dfm.tfidf <- dfm_tfidf(corpus_tokens.dfm)
View(corpus_tokens.dfm.tfidf[1:5, 1:5])
corpus_tokens.tfidf <- convert(corpus_tokens.dfm.tfidf, to = "lsa")

# Transpose the matrix
corpus_tokens.tfidf <- t(corpus_tokens.tfidf)

# Centering the data
corpus_tokens.tfidf.colmean <- apply(corpus_tokens.tfidf, 2, mean)
corpus_tokens.tfidf.centered <-
  corpus_tokens.tfidf - corpus_tokens.tfidf.colmean

# Variance explained
pca <- prcomp(corpus_tokens.tfidf.centered)
summary(pca)

# Perform SVD. Specifically, reduce dimensionality down to 2 columns
# for our latent semantic analysis (LSA).
corpus_irlba <-
  irlba(t(corpus_tokens.tfidf.centered),
        nv = 75,
        maxit = 600)
str(corpus_irlba)

# Cosine matrix comparing all countries to each other
corpus_similarities <- cosine(t(as.matrix(corpus_svd[, -1])))

# Add countries label to dataframe
corpus_df <- corpus_svd
row.names(corpus_df) <- countries
corpus_df <- corpus_df[, -1]

# Computes distance matrix based on cosine similarities
cdist <- as.dist(1 - corpus_similarities)

# Plotting hierarchal clustering
hc <- hclust(cdist, "ward.D2")
clustering <- cutree(hc, 35)
plot(
  hc,
  main = "Hierarchical clustering of 193 Countries",
  ylab = "",
  xlab = "",
  yaxt = "n"
)
rect.hclust(hc, 35, border = "red")

# Grouping the clusters
cluster_df <- data.frame(cbind(Countries = countries, clustering))
cluster_grouped <-
  aggregate(countries ~ clustering, data = cluster_df, c)
View(cluster_grouped)