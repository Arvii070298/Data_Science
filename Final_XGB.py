#!/usr/bin/env python
# coding: utf-8

# In[1]:


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


# In[2]:


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


# In[3]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']
X_train


# In[4]:


from numpy import loadtxt
from xgboost import XGBClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.metrics import recall_score

xtra = X_train.values
xtes = X_test.values
model = XGBClassifier()

model.fit(xtra, Y_train)
pred = model.predict(xtes)

pred

X_test


# In[ ]:

accuracy = accuracy_score(Y_test, pred)
print('accuracy',accuracy)

precision = precision_score(Y_test,pred, average='weighted')
print ('precision',precision)
recall=recall_score(Y_test, pred, average='weighted')
print('Recall', recall)

f1=f1_score(Y_test, pred, average='weighted')

print ('f1 =',f1)




# In[ ]:




