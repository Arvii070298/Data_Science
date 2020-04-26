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


# In[3]:


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


# In[4]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']
X_train


# In[6]:


from sklearn.ensemble import RandomForestClassifier

from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score

forest = RandomForestClassifier(n_estimators=1, random_state=345)

forest = forest.fit(X_train, Y_train)


print("training data accuracy = ",forest.score(X_train, Y_train))
print("testing data accuracy = ",forest.score(X_test, Y_test))

Y_pred_RF = forest.predict(X_test)
df = pd.DataFrame(Y_pred_RF, columns = ['Dwd'])

export_csv = df.to_csv (r'RaFo.csv', index = None, header=True) 

# accuracy: (tp + tn) / (p + n)
accuracy = accuracy_score(Y_test, Y_pred_RF)
print('Accuracy = ', accuracy)

# precision tp / (tp + fp)
precision = precision_score(Y_test, Y_pred_RF)
print('Precision = ', precision)

# f1: 2 tp / (2 tp + fp + fn)
f1 = f1_score(Y_test, Y_pred_RF)
print('F1 score = ', f1)





# In[ ]:




