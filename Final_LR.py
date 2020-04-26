#!/usr/bin/env python
# coding: utf-8

# In[20]:


import pandas as pd

from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.ensemble import RandomForestRegressor

from sklearn.metrics import recall_score

from sklearn.metrics import precision_score
from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score



import numpy as np
#mathematical operations


# In[440]:


data_test = pd.read_csv('predict_test.csv')



# In[365]:


data_train = pd.read_csv('predict_train.csv')


# In[366]:


data_test


# In[367]:


data1_train=data_train.drop([458])


# In[368]:


data1_train









# In[370]:


Y_train=data1_train['DwD']
X_train= data1_train[['name','gender','popularity']]

X_test= data_test[['name','gender','popularity']]
Y_test= data_test['DwD']



s = pd.get_dummies(X_train['name'])


# In[371]:


u = pd.get_dummies(X_test['name'])


# In[372]:


X_train.drop(['name'], axis=1)


# In[373]:


X_test.drop(['name'], axis=1)


# In[374]:


a = X_train.join(s)


# In[375]:


a




c = a.drop(['name'], axis=1)


# In[378]:


c


# In[379]:


X_train = c


# In[380]:


X_train


# In[381]:


d = X_test.join(u)


# In[382]:


d


# In[383]:


f = d.drop(['name'], axis=1)


# In[384]:


f


# In[385]:


X_test= f

# In[386]:


X_test


# In[ ]:





# In[ ]:







X_train.shape


# In[392]:


X_test.shape


# In[393]:


X_test


# In[394]:


X_train.shape


# In[ ]:





# In[395]:


Y_train.shape





X_test.shape



Y_test.shape



# In[ ]:





# In[21]:


from sklearn.linear_model import LogisticRegression


# In[963]:


logreg = LogisticRegression(C=1e10)


# In[115]:


fit = logreg.fit(X_train, Y_train)


# In[964]:
Y_pred_LR = fit.predict(X_test)
# In[139]:


accuracy = accuracy_score(Y_test, Y_pred_LR)
print('accuracy',accuracy)


# In[140]:


precision = precision_score(Y_test, Y_pred_LR)
print ('precision',precision)


# In[141]:


f1=f1_score(Y_test, Y_pred_LR)


# In[142]:


print ('f1 =',f1)




# In[19]:



import re

abc = []
for i in range(0,458):
    xy = str(data_test['name'][i])
    ab = xy.replace(" ", "_" )
    abc.append(ab)
    




df = pd.DataFrame({'name': abc,
                   'dwd': Y_pred_LR
                  })

df['c1'] = ''
df['c1'] = 0
df['c2'] = ''
df['c2'] = 0

df['c5'] = ''
# In[ ]:
df['c5'] = 1
df['c6'] = ''
df['c6'] = 'team_7'
df = df[['c1', 'c2', 'name', 'dwd', 'c5', 'c6']]
df1 = df[1:]
# In[ ]:




export_csv = df.to_csv (r'LoRe.run', index = None, header=None, sep=' ') 


# In[ ]:




