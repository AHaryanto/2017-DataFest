dataPath <- "~/Documents/Interest Rates"
Data <-
  read.csv(
    file = paste(dataPath, "InterestRates.csv", sep = "/"),
    row.names = 1,
    header = TRUE,
    sep = ","
  )


# ************************************ Part 1: Simple Regression ************************************


# Fit 7 simple regression models, in each of them one input variable of
# interest rate from AssignmentData is response and variable Output1
# from the same data is the single predictor.
mUSGG3M <- lm(USGG3M ~ Output1, data = Data)
mUSGG6M <- lm(USGG6M ~ Output1, data = Data)
mUSGG2YR <- lm(USGG2YR ~ Output1, data = Data)
mUSGG3YR <- lm(USGG3YR ~ Output1, data = Data)
mUSGG5YR <- lm(USGG5YR ~ Output1, data = Data)
mUSGG10YR <- lm(USGG10YR ~ Output1, data = Data)
mUSGG30YR <- lm(USGG30YR ~ Output1, data = Data)

newPredictor <- data.frame(Output1 = -11.665) #arbitrary new predictor value

# predict 7 interest rates for the value of newPredictor
prUSGG3M <- predict(mUSGG3M, newdata = newPredictor)
prUSGG6M <- predict(mUSGG6M, newdata = newPredictor)
prUSGG2YR <- predict(mUSGG2YR, newdata = newPredictor)
prUSGG3YR <- predict(mUSGG3YR, newdata = newPredictor)
prUSGG5YR <- predict(mUSGG5YR, newdata = newPredictor)
prUSGG10YR <- predict(mUSGG10YR, newdata = newPredictor)
prUSGG30YR <- predict(mUSGG30YR, newdata = newPredictor)

predicted.values <-
  c(`prUSGG3M`,`prUSGG6M`,`prUSGG2YR`,`prUSGG3YR`,`prUSGG5YR`,`prUSGG10YR`,`prUSGG30YR`)


# ************************************ Part 2: Logistic Regression ************************************


data <-
  readRDS(paste(
    dataPath,
    'Predictor_Data.rds',
    sep = '/'
  ))
newPredictors <- data$Predictor
newPredictors <-
  setNames(newPredictors, colnames(DataLogistic[1:7]))
newPredictors <- as.data.frame(t(newPredictors))

DataLogistic <- Data[, c(1:7, 10)]

# Make column “Tightening” equal to 1 during tightening periods and 0 otherwise.
All.NAs <-
  is.na(Data[, 9]) &
  is.na(Data[, 10]) # neither tightening nor easing
noTightening <- is.na(Data[, 10])
DataLogistic[noTightening, 'Tightening'] <-0 # replace NAs with 0

# Remove periods of neither tightening nor easing
cat("Before: ", dim(DataLogistic), "\n") # before
DataLogistic <- DataLogistic[!All.NAs,]
cat("After: ", dim(DataLogistic), "\n") # after removing neutral periods
DataLogistic[c(275:284),]

# Estimate logistic regression with all yields as predictor and Tightening as output
LogisticModel_All <-
  glm(Tightening ~ ., family = binomial(link = logit), DataLogistic)

# Estimate logistic regression with all 10YR as predictor and Tightening as output
LogisticModel_10YR <-
  glm(Tightening ~ USGG10YR,family = binomial(link = logit), DataLogistic)

#1. Probability of tightening
predict(LogisticModel_All, newdata = newPredictors, type = 'response')

#2. Relative change of odds ratio (%) when USGG10YR changes by 1%
beta_10YR <- LogisticModel_All$coefficients["USGG10YR"]
(exp(beta_10YR * 0.01)) * 100


# ************************************ Part 3: Comparing Regression Models ************************************


AssignmentDataRegressionComparison<-AssignmentData[,-c(9,10)]
testrate <- (Data$USGG5YR) #arbitrary yield

#Compare model Output1~testRate and model Output~testRate+additionalRate using regression ANOVA
modeltest <- lm(Output1 ~ testrate, data = Data)
modeladditionalrate1 <-
  lm(Output1 ~ testrate + USGG3M, data = Data)
modeladditionalrate2 <-
  lm(Output1 ~ testrate + USGG6M, data = Data)
modeladditionalrate3 <-
  lm(Output1 ~ testrate + USGG2YR, data = Data)
modeladditionalrate4 <-
  lm(Output1 ~ testrate + USGG3YR, data = Data)
modeladditionalrate5 <-
  lm(Output1 ~ testrate + USGG10YR, data = Data)
modeladditionalrate6 <-
  lm(Output1 ~ testrate + USGG30YR, data = Data)

# Value of sum of squares showing advantage of the bigger model relative to the smaller model
v1 <- anova(modeltest, modeladditionalrate1)$"Sum of Sq"[2]
v2 <- anova(modeltest, modeladditionalrate2)$"Sum of Sq"[2]
v3 <- anova(modeltest, modeladditionalrate3)$"Sum of Sq"[2]
v4 <- anova(modeltest, modeladditionalrate4)$"Sum of Sq"[2]
v5 <- anova(modeltest, modeladditionalrate5)$"Sum of Sq"[2]
v6 <- anova(modeltest, modeladditionalrate6)$"Sum of Sq"[2]
SumSq <- c(v1, v2, v3, v4, v5, v6)

# Calculate AIC for all models Output~testRate+additionalRate
Selected.US.Treasury <- "USGG3M"
AIC1 <- AIC(modeladditionalrate1)
AIC2 <- AIC(modeladditionalrate2)
AIC3 <- AIC(modeladditionalrate3)
AIC4 <- AIC(modeladditionalrate4)
AIC5 <- AIC(modeladditionalrate5)
AIC6 <- AIC(modeladditionalrate6)
AIC <- c(AIC1, AIC2, AIC3, AIC4, AIC5, AIC6)


# ************************************ Part 4: Rolling Window Analysis ************************************

#install.packages('zoo')
library(zoo)
Window.width <- 20
Window.shift <- 5

testDate <- '7/28/1982' #arbitrary test date

# Rolling lm coefficients for the model with Output1 as response and USGG3M, USGG5YR, USGG30YR 
# as predictors fitted around testDate using Window.width=20 and Window.shift=5:
Coefficients <-
  rollapply(
    DataRegressionComparison,
    width = Window.width,
    by = Window.shift,
    by.column = FALSE,
    FUN = function(z)
      coef(lm(
        Output1 ~ USGG3M + USGG5YR + USGG30YR,
        data = as.data.frame(z)
      ))
  )

rolling.dates <- rollapply(
  DataRegressionComparison[, 1:8],
  width = Window.width,
  by = Window.shift,
  by.column = FALSE,
  FUN = function(z)
    rownames(z)
)

rownames(Coefficients) <- rolling.dates[, 10]
cfnts <- Coefficients[testDate, ]

# Coefficients analysis shows that the response Output1 had the highest sensitivity 
# to USGG5YR input rate around 7/28/1982 test date
highestSensitivity <- 'USGG5YR'

# R-squaredfor the model with Output1 as response and USGG3M, USGG5YR, USGG30YR 
# as predictors fitted around testDate using Window.width=20 and Window.shift=5:
r.squared <- rollapply(
  DataRegressionComparison,
  width = Window.width,
  by = Window.shift,
  by.column = FALSE,
  FUN = function(z)
    summary(lm(
      Output1 ~ USGG3M + USGG5YR + USGG30YR,
      data = as.data.frame(z)
    ))$r.squared
)
r.squared <- data.frame(Date = rolling.dates[, 10], R2 = r.squared)

idxDate <- match(testDate, rolling.dates[, 10])

rsqrd <- r.squared[idxDate, 2]

# P-values for the model with Output1 as response and USGG3M, USGG5YR, USGG30YR 
# as predictors fitted around testDate using Window.width=20 and Window.shift=5:
Pvalues <-
  rollapply(
    DataRegressionComparison,
    width = Window.width,
    by = Window.shift,
    by.column = FALSE,
    FUN = function(z)
      summary(lm(
        Output1 ~ USGG3M + USGG5YR + USGG30YR,
        data = as.data.frame(z)
      ))$coefficients[, 4]
  )
rownames(Pvalues) <- rolling.dates[, 10]

pvls <- Pvalues[testDate, ]

# Prediction of Output1 on testDate
DataRegressionComparison[testDate, ]
prediction <-
  -17.6539976 + (0.7765271 * 10.72) + (1.3794229 * 14.004) + (0.6895046 *
                                                                13.602)


# ************************************ Part 5: Principal Components Analysis ************************************


data <-
  readRDS(paste(
    dataPath,
    'PCA_Data.rds',
    sep = '/'
  ))
DataPCA <- Data[, 1:7]

PCA.Yields <- princomp(DataPCA)
pcaLoadings <- PCA.Yields$loadings[, 1:3]
pcaLoading0 <- PCA.Yields$center

# 3 PCA loadings for testMaturity:
loadValue <- pcaLoadings[data$testMaturity, ]

# 3 PCA factors corresponding to testDate:
pcaFactors <- PCA.Yields$scores[, 1:3]
factValue <- pcaFactors[data$testDate, 1:3]

# Predict value of time series testMaturity on testDate:
predictValue <-
  pcaLoading0[['USGG5YR']] + pcaLoadings['USGG5YR', 'Comp.1'] * pcaFactors[data$testDate, 'Comp.1'] + pcaLoadings['USGG5YR', 'Comp.2'] * pcaFactors[data$testDate, 'Comp.2'] +
  pcaLoadings['USGG5YR', 'Comp.3'] * pcaFactors[data$testDate, 'Comp.3']

# Residual of the model on testDate for testMaturity:
residValue <-
  DataPCA[data$testDate, data$testMaturity] - predictValue

# Changes in all 7 rates of the term curve corresponding to the given increments of the 3 factors:
ModelCurveAdjustment.3Factors <-
  t(pcaLoadings[, 1]) * data$factorsChanges[['Comp.1']] + t(pcaLoadings[, 2]) * data$factorsChanges[['Comp.2']] +
  t(pcaLoadings[, 3]) * data$factorsChanges[['Comp.3']]


