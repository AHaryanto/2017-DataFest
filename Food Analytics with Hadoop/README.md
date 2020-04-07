### Recipe Classification

A proposed big data machine learning model that classifies recipes into healthy or unhealthy categories based on the recipe nutrition to help prevent heart disease. Data is extracted from three different data sets - Open Food Facts, Christmas Recipes, General Recipes. The overall data is over 1GB in size. Data engineering and machine learning are done using PySpark and Hive on Google Cloud Platform.

**Methodology:**
1. Get the top 20 ingredients from Christmas Recipes and General Recipes
2. Merge Open Food Facts dataset with recipes containing the top 20 ingredients from step 1
3. Normalize nutrient values
4. Run machine learning classification algorithms

**Infrastructure:**
- 4 node Dataproc cluster
- 1 master, 4 workers
- 4 vCPUs 9 GB memory


Credit to Sarah Qin, Tobel Eze-Okoli, and Valeri Antonova.
