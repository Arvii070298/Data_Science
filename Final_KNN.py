#!/usr/bin/env python
# coding: utf-8

# In[19]:


import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score
from sklearn.metrics import f1_score
import numpy as np

final_test = pd.read_csv('final_test.csv')

final_train = pd.read_csv('final_train.csv')

final_test


# In[20]:


final_train


# In[21]:


final_test


# In[22]:


q = pd.concat([final_train, final_test])
from sklearn import preprocessing
le = preprocessing.LabelEncoder()
w = le.fit_transform(q['Allegiance'])
w = pd.DataFrame([w])
w = w.T
w = w.rename({0: 'Allegiance'}, axis='columns')
q = q.drop(['Allegiance'], axis=1)
q = q.reset_index()
q = q.drop(['index'], axis=1)
e = pd.concat([q, w], axis=1)
t = e.iloc[:459,:]
y = e.iloc[459:,:]
final_train = t
final_test = y
final_test = final_test.reset_index()
final_test = final_test.drop(['index'], axis=1)
final_train


# In[23]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']
X_train


# In[24]:


from sklearn.neighbors import KNeighborsClassifier
classifier = KNeighborsClassifier(n_neighbors=5)
classifier.fit(X_train, Y_train)

y_pred_knn = classifier.predict(X_test)

print('score = ',classifier.score(X_test, Y_test))

accuracy = accuracy_score(Y_test,y_pred_knn)
print('accuracy',accuracy)

x = precision_score(Y_test, y_pred_knn)

y=recall_score(Y_test, y_pred_knn)
print('Recall', y)
z=f1_score(Y_test, y_pred_knn)
#Precision score

print ('Precision',z)
print ('f1',x)


# In[ ]:





# In[ ]:




