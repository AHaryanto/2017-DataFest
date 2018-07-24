#data <- read.delim("~/Documents/DataFest 2017/DataFest2017/Data/data.txt",sep="\t",stringsAsFactors = F)
#dest <- read.delim("~/Documents/DataFest 2017/DataFest2017/Data/dest.txt",sep="\t",stringsAsFactors = F)
data_df <- data.frame(data)
#data_df1 <- data.frame(data)
#dest_df <- data.frame(dest)

#comparing hist_price_band single traveler vs family 
#data_txt_df_SvsF <- data_txt_df[data_txt_df$is_booking == 1,c(15,16,17,20,25)]
#data_txt_df_SvsF <- data_txt_df_SvsF[order(data_txt_df_SvsF$hist_price_band,decreasing=TRUE),]

#merging dest and data on srch_destinantion_id
#lookup <- dest_txt_df[,c(1,2)]
#data_txt_df2 <- merge(lookup,data_txt_df,by='srch_destination_id')


#is_booking == 1
data_df <- data_df[data_df$is_booking==1,]
#data_df1 <- data_df[data_df1$is_booking==1,]
#srch_children_cnt==0
data_df <- data_df[data_df$srch_children_cnt==0,]

#adding day variable to data_df
data_df$check_in_day <- weekdays(as.Date(data_df$srch_ci))
data_df$check_out_day <- weekdays((as.Date(data_df$srch_co)))

#selecting M-F check-in
data_df <- data_df[(data_df$check_in_day=="Monday"|data_df$check_in_day=="Tuesday"|data_df$check_in_day=="Wednesday"|data_df$check_in_day=="Thursday"|data_df$check_in_day=="Friday"),]

#country is U.S.
data_df <- data_df[data_df$hotel_country=="UNITED STATES OF AMERICA",]

#formatting check-in date
temp <- as.Date(data_df[,13],"%Y-%m-%d")
short_date <- format(temp, format="%m-%d")
data_df$shortdatecheckin <- short_date
temp <- as.Date(data_df[,14],"%Y-%m-%d")
short_date <- format(temp, format="%m-%d")
data_df$shortdatecheckout <- short_date

#getting rid of Christmas
data_df <- subset(data_df, !(shortdatecheckin %in% 
                                          c("12-24","12-25","12-26","12-27","12-28","12-29","12-30","12-31")))
data_df <- subset(data_df, !(shortdatecheckout %in% 
                                                     c("12-26","12-27","12-28","12-29","12-30","12-31","01-01")))

#formatting date_time
date_time_short <- as.Date(data_df$date_time,"%Y-%m-%d %H:%M:%S")
date_time_short <- format(as.POSIXct(date_time_short,format='%Y/%d/%d'),format='%Y/%m/%d')
data_df$date_time_short <- date_time_short

#check-in - book
checkInMinusBook <- as.Date((data_df$srch_ci), format="%Y-%m-%d") - 
  as.Date((data_df$date_time_short), format="%Y/%m/%d")
#View(checkInMinusBook)
data_df$checkInMinusBook <- checkInMinusBook

#select checkInMinusBook < 14
data_df_MF <- data_df[data_df$checkInMinusBook<14,]

#pie chart
slices <- c(373301,958896) 
lbls <- c("Business Traveleres", "Non-Business Travelers")
pct <- round(slices/sum(slices)*100)
lbls <- paste(lbls, pct) # add percents to labels 
lbls <- paste(lbls,"%",sep="") # ad % to labels 
pie(slices,labels = lbls, col=rainbow(length(lbls)),
    main="Business Traveleres Percentage")


