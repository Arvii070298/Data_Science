#!/usr/bin/env python
# coding: utf-8

# In[17]:


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


# In[18]:


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


# In[19]:


X_train = final_train.drop(columns=['name', 'DwD'])
Y_train = final_train['DwD']
X_test = final_test.drop(columns=['name', 'DwD'])
Y_test = final_test['DwD']
X_train


# In[20]:


from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import confusion_matrix
from sklearn.ensemble import BaggingClassifier

# In[1159]:


dtc = DecisionTreeClassifier()
bag_model=BaggingClassifier()
bag_model=bag_model.fit(X_train,Y_train)


# In[1160]:


ytest_pred=bag_model.predict(X_test)


df = pd.DataFrame(ytest_pred, columns = ['DwD'])

#export_csv = df.to_csv (r'BagC.run', index = None, header=True)

x = precision_score(Y_test, ytest_pred)
y=f1_score(Y_test, ytest_pred)
z=recall_score(Y_test, ytest_pred)

# In[1161]:


print(bag_model.score(X_test, Y_test))

print(confusion_matrix(Y_test, ytest_pred)) 


# In[1162]:


accuracy = accuracy_score(Y_test, ytest_pred)
print('accuracy',accuracy)


# In[1163]:



print ('precision',y)
print('Recall', z)
print ('f1',x)





# In[ ]:





# In[ ]:





# In[ ]:




