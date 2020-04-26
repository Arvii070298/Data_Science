#!/usr/bin/env python
# coding: utf-8

# In[171]:


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


# In[172]:


final_train


# In[173]:


final_train


# In[174]:


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


# In[175]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']
X_train


# In[176]:


X_train


# In[ ]:





# In[177]:


from sklearn import tree



tree1 = tree.DecisionTreeClassifier()

tree1 = tree1.fit(X_train, Y_train)

print('the accuracy rate on training data is', tree1.score(X_train, Y_train))

print('the accuracy rate on testing data is', tree1.score(X_test, Y_test))

Y_pred_DT = tree1.predict(X_test)

df = pd.DataFrame(Y_pred_DT, columns = ['DwD'])
export_csv = df.to_csv (r'DeTr.csv', index = None, header=True) 
accuracy = accuracy_score(Y_test, Y_pred_DT)
print('accuracy',accuracy)
y=f1_score(Y_test, Y_pred_DT)
print ('precision',y)
x = precision_score(Y_test, Y_pred_DT)
print ('f1',x)



# In[ ]:




