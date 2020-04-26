#!/usr/bin/env python
# coding: utf-8

# In[313]:


import numpy as np
import pandas as pd
from sklearn.base import RegressorMixin


# In[314]:


final_train = pd.read_csv('final_train.csv')


# In[315]:


final_test = pd.read_csv('final_test.csv')


# In[316]:


final_train


# In[317]:


final_test


# In[318]:


X = final_train.drop(['DwD'], axis=1)


# In[319]:


Y = final_train['DwD']


# In[320]:


X


# In[321]:


Y


# In[322]:


q = pd.concat([final_train, final_test])


# In[323]:


from sklearn import preprocessing
le = preprocessing.LabelEncoder()


# In[324]:


w = le.fit_transform(q['Allegiance'])
w = pd.DataFrame([w])
w = w.T
w = w.rename({0: 'Allegiance'}, axis='columns')
w


# In[325]:


q = q.drop(['Allegiance'], axis=1)
q = q.reset_index()
q = q.drop(['index'], axis=1)
e = pd.concat([q, w], axis=1)
e


# In[326]:


t = e.iloc[:459,:]
y = e.iloc[459:,:]
final_train = t
final_test = y
final_test = final_test.reset_index()
final_test = final_test.drop(['index'], axis=1)


# In[327]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']


# In[ ]:





# In[ ]:





# In[ ]:





# In[ ]:





# In[328]:


from sklearn.model_selection import train_test_split


# In[329]:


X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.3, random_state=123)


# In[330]:


X_train.shape, X_test.shape, Y_train.shape, Y_test.shape


# In[ ]:





# In[331]:


#X_new = X.drop(['name'], axis=1)


# In[332]:


#X_new


# # KMeans Transformer

# In[333]:


from sklearn.base import TransformerMixin
from sklearn.cluster import KMeans
class KMeansTransformer(TransformerMixin):
    def __init__(self, *args, **arg):
        self.model = KMeans(*args, **arg)


# In[334]:


from sklearn.preprocessing import OneHotEncoder

class KMeansTransformer(TransformerMixin):
    def __init__(self, *args, **arg):
        self.model = KMeans(*args, **arg)
    def fit(self, X):
        self.X = X
        self.model.fit(X)
    def transform(self, X):
        # Need to reshape into a column vector in order to use
        # the onehot encoder.
        cl = self.model.predict(X).reshape(-1, 1)
        
        self.oh = OneHotEncoder(
            categories="auto", 
            sparse=False,
            drop="first"
        )
        cl_matrix = self.oh.fit_transform(cl)      
 
        return np.hstack([self.X, cl_matrix])
    def fit_transform(self, X, y=None):
        self.fit(X)
        return self.transform(X)


# In[335]:


from sklearn.preprocessing import StandardScaler
from sklearn.linear_model import LogisticRegression
from sklearn.datasets import make_blobs
from sklearn.pipeline import Pipeline
X, y = make_blobs(
    n_samples=100,
    n_features=2,
    centers=3
)
pipe = Pipeline([
    ("sc", StandardScaler()),
    ("km", KMeansTransformer()),
    ("lr", LogisticRegression(penalty="none", solver="lbfgs"))
])


# In[336]:


pipe.fit(X_new, Y)


# In[337]:


pipe.score(X_new, Y)


# In[ ]:




