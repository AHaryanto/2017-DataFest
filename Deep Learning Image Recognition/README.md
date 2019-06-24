### Deep Learning Image Recognition

A deep learning project trying to determine if two individuals are related given their pictures.

Approach:
1. Calculate euclidean distance using FaceNet kernel
2. Extract features such as age and gender using OpenCV pre-trained model
3. Use VGG Face pre-trained model to calculate cosine similarity
4. Feed output of 1,2,3 and image properties as features to XGBoost

Credit to Casey_Works, Dan Tallarico, and Saurabh Srivastava.
