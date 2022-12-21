# Thesis NYC TLC Data Generator
 This program generates the aggregated data found in the Dataset Tool's data.sql file.
 NOTE: The main application already contains the necessary data to run in its data.sql file, you should only change this file or use this generator program if you wish to change the trip record data being used.

## How to use
First, you will need to visit https://www.nyc.gov/site/tlc/about/tlc-trip-record-data.page and download the trip record data for the years of your choice.

Second, you can adjust the sql statements for each type of trip to change the desired output (optional).

Third, change where the trip data is pulled from so the file path matches your local machine. E.g., https://github.com/devitoa2/Thesis-NYC-TLC-Data-Generator/blob/main/FHVData.java#L23

Fourth, change the buffered writer line to use the desired path on your local machine where you'd like the data files to be written to. E.g., https://github.com/devitoa2/Thesis-NYC-TLC-Data-Generator/blob/main/FHVData.java#L36

Fifth, uncomment the file writing programs from main you would like to use.

Sixth, once the file finishes writing, you will need to change the final comma at the bottom, right-most of the screen to a semi-colon. Then you can copy this data and paste it into the data.sql file in the main application. https://github.com/devitoa2/Thesis-NYC-TLC-Dataset-Tool/blob/main/src/main/resources/data.sql
NOTE: The above file already contains the necessary data to run the application, you should only change this file or use this generator program if you wish to change the trip record data being used.
