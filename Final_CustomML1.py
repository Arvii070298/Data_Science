#!/usr/bin/env python
# coding: utf-8

# In[340]:


import numpy as np
import pandas as pd
from sklearn.base import RegressorMixin


# In[341]:


final_train = pd.read_csv('final_train.csv')


# In[342]:


final_test = pd.read_csv('final_test.csv')


# In[343]:


final_train


# In[344]:


final_test


# In[345]:


X = final_train.drop(['DwD'], axis=1)


# In[346]:


Y = final_train['DwD']


# In[347]:


X


# In[348]:


q = pd.concat([final_train, final_test])


# In[349]:


from sklearn import preprocessing
le = preprocessing.LabelEncoder()


# In[350]:


w = le.fit_transform(q['Allegiance'])


# In[351]:


w = pd.DataFrame([w])
w = w.T
w = w.rename({0: 'Allegiance'}, axis='columns')


# In[352]:


w


# In[353]:


q = q.drop(['Allegiance'], axis=1)


# In[354]:


q = q.reset_index()


# In[355]:


q = q.drop(['index'], axis=1)


# In[356]:


e = pd.concat([q, w], axis=1)
e


# In[357]:


t = e.iloc[:459,:]


# In[358]:


y = e.iloc[459:,:]


# In[359]:


final_train = t


# In[360]:


final_test = y


# In[361]:


final_test = final_test.reset_index()


# In[362]:


final_test = final_test.drop(['index'], axis=1)


# In[363]:


final_train


# In[364]:


final_test


# In[367]:


X_train = final_train.drop(columns=['name', 'DwD'])


# In[368]:


Y_train = final_train['DwD']


# In[370]:


X_test = final_test.drop(columns=['name', 'DwD'])


# In[371]:


Y_test = final_test['DwD']


# In[372]:


from sklearn.model_selection import train_test_split


# In[373]:


#X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.3, random_state=123)


# In[374]:


X_train.shape, X_test.shape, Y_train.shape, Y_test.shape


# In[375]:


# ML_Custom method1_NullRegressor

class NullRegressor(RegressorMixin):
    def fit(self, X=None, y=None):
        
        self.y_bar_ = np.mean(y)
    def predict(self, X=None):
        
        return np.ones(X.shape[0]) * self.y_bar_


# In[376]:


model = NullRegressor()


# In[377]:


model.fit(X_train, Y_train)


# In[378]:


Y_pred = model.predict(X_test)


# In[379]:


len(Y_pred)


# In[380]:


for i in range(0,458):
    if(Y_pred[i] >0):
        Y_pred[i] = 1


# In[381]:


from sklearn.metrics import accuracy_score
score = accuracy_score(Y_test, Y_pred)
print("accuracy score",score)  


# In[382]:


from sklearn.metrics import f1_score
f1_score = f1_score(Y_test, Y_pred)
print("f1 score",f1_score)  


# In[ ]:




