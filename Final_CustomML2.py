#!/usr/bin/env python
# coding: utf-8

# In[260]:


import pandas as pd


# In[261]:


data_train = pd.read_csv('gender_tf.csv')


# In[262]:


data_test = pd.read_csv('gender_tf_testt.csv')


# In[ ]:





# In[263]:


final_train = pd.read_csv('predict_train.csv')


# In[264]:


final_test = pd.read_csv('predict_test.csv')


# In[ ]:





# In[265]:


alle_train = pd.read_csv('Allegiance_train.csv')


# In[266]:


alle_test = pd.read_csv('Allegiance_test.csv')


# In[ ]:





# In[191]:


data_train


# In[192]:


data_test


# In[259]:


X = data_train.drop(['Name'], axis=1)


# In[257]:


data_train


# In[194]:


X = X.drop(['gender'], axis=1)


# In[195]:


X


# In[196]:


Y = data_train['gender']


# In[197]:


Y


# In[198]:


from sklearn.model_selection import train_test_split


# In[199]:


#X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.3, random_state=123)


# In[200]:


from sklearn import tree


# In[201]:


tree = tree.DecisionTreeClassifier(max_depth = 2)


# In[202]:


tree1 = tree.fit(X, Y)


# In[203]:


import numpy as np


# In[204]:


testdata_f = data_test['score']


# In[205]:


testdata_f = np.reshape(testdata_f.values, (-1, 1))


# In[206]:


testdata_l = data_test['gender']


# In[ ]:





# In[207]:


Y_pred = tree1.predict(testdata_f)


# In[208]:


Y_pred


# In[209]:


len(Y_pred)


# In[210]:


from sklearn.metrics import accuracy_score
from sklearn.metrics import f1_score


# In[211]:


accuracy = accuracy_score(testdata_l, Y_pred)
f1_score = f1_score(testdata_l, Y_pred)


# In[212]:


print('accuracy = ', accuracy)
print('f1_score = ', f1_score)


# In[ ]:





# In[ ]:





# In[213]:


n = pd.DataFrame(Y_pred)


# In[214]:


m = n.rename({0: 'gender2'}, axis='columns')


# In[215]:


m


# In[216]:


o = pd.DataFrame(data_train['gender'])


# In[217]:


o = o.rename({'gender': 'gender2'}, axis='columns')


# In[218]:


o


# In[ ]:





# In[ ]:





# In[219]:


#final_train, final_test        o, n


# In[220]:


final_train = pd.concat([final_train, o], axis=1)


# In[221]:


final_train


# In[222]:


final_test = pd.concat([final_test, m], axis=1)


# In[223]:


final_test


# In[ ]:





# In[224]:


alle_train


# In[225]:


alle_test


# In[ ]:





# In[226]:


final_train = pd.concat([final_train, pd.DataFrame(alle_train['Allegiance'])], axis=1)


# In[227]:


final_test = pd.concat([final_test, pd.DataFrame(alle_test['Allegiance'])], axis=1)


# In[228]:


final_train


# In[229]:


final_test


# In[117]:


q = pd.concat([final_train, final_test])


# In[118]:


q


# In[119]:


from sklearn import preprocessing
le = preprocessing.LabelEncoder()


# In[127]:


w = le.fit_transform(q['Allegiance'])


# In[133]:


w = pd.DataFrame([w])


# In[135]:


w = w.T


# In[137]:


w = w.rename({0: 'Allegiance'}, axis='columns')


# In[ ]:





# In[131]:


q = q.drop(['Allegiance'], axis=1)


# In[151]:


q = q.reset_index()


# In[154]:


q = q.drop(['index'], axis=1)


# In[ ]:





# In[158]:


e = pd.concat([q, w], axis=1)


# In[159]:


e


# In[160]:


t = e.iloc[:459,:]


# In[161]:


y = e.iloc[459:,:]


# In[166]:


final_train = t


# In[167]:


final_test = y


# In[170]:


final_test = final_test.reset_index()


# In[172]:


final_test = final_test.drop(['index'], axis=1)


# In[173]:


final_train


# In[174]:


final_test


# In[ ]:





# In[175]:


X_train = final_train.drop(columns=['name', 'DwD'])


# In[177]:


Y_train = final_train['DwD']


# In[178]:


X_test = final_test.drop(columns=['name', 'DwD'])


# In[179]:


Y_test = final_test['DwD']


# In[ ]:





# In[180]:


X_train


# In[181]:


Y_train


# In[182]:


X_test


# In[183]:


Y_test


# In[245]:


datase = data_train.drop(['Name'], axis=1)


# In[246]:


dataset = datase.values.tolist()


# In[278]:


final_train


# In[279]:


final_test


# In[281]:


datase1 = final_train.drop(['name','gender'], axis=1)


# In[282]:


dataset1 = datase1.values.tolist()


# In[283]:


from random import seed
from random import randrange
from csv import reader
from math import sqrt


# In[284]:


# Calculating root mean squared error
def rmse_metric(actual, predicted):
    sum_error = 0.0
    for i in range(len(actual)):
        prediction_error = predicted[i] - actual[i]
        sum_error += (prediction_error ** 2)
    mean_error = sum_error / float(len(actual))
    return sqrt(mean_error)


# In[275]:


# Evaluate regression algorithm on training dataset
def evaluate_algorithm(dataset, algorithm):
    test_set = list()
    for row in dataset:
        row_copy = list(row)
        row_copy[-1] = None
        test_set.append(row_copy)
    predicted = algorithm(dataset, test_set)
    print(predicted)
    actual = [row[-1] for row in dataset]
    f = rmse_metric(actual, predicted)
    return f


# In[285]:


def evaluate_algorithm(dataset1, algorithm):
    test_set = list()
    for row in dataset1:
        row_copy = list(row)
        row_copy[-1] = None
        test_set.append(row_copy)
    predicted = algorithm(dataset1, test_set)
    print(predicted)
    actual = [row[-1] for row in dataset]
    f = rmse_metric(actual, predicted)
    return f


# In[286]:


# Calculate the mean value of a list of numbers
def mean(values):
    return sum(values) / float(len(values))


# In[287]:


# Calculate covariance between x and y
def covariance(x, mean_x, y, mean_y):
    covar = 0.0
    for i in range(len(x)):
        covar += (x[i] - mean_x) * (y[i] - mean_y)
    return covar


# In[288]:


# Calculate the variance of a list of numbers
def variance(values, mean):
    return sum([(x-mean)**2 for x in values])


# In[253]:


# Calculate coefficients
def coefficients(dataset):
    x = [row[0] for row in dataset]
    y = [row[1] for row in dataset]
    x_mean, y_mean = mean(x), mean(y)
    b1 = covariance(x, x_mean, y, y_mean) / variance(x, x_mean)
    b0 = y_mean - b1 * x_mean
    return [b0, b1]


# In[289]:


# Calculate coefficients
def coefficients(dataset1):
    x = [row[0] for row in dataset1]
    y = [row[1] for row in dataset1]
    x_mean, y_mean = mean(x), mean(y)
    b1 = covariance(x, x_mean, y, y_mean) / variance(x, x_mean)
    b0 = y_mean - b1 * x_mean
    return [b0, b1]


# In[290]:


# Simple linear regression algorithm
def simple_linear_regression(train, test):
    predictions = list()
    b0, b1 = coefficients(train)
    for row in test:
        yhat = b0 + b1 * row[0]
        predictions.append(yhat)
    return predictions


# In[277]:


f = evaluate_algorithm(dataset, simple_linear_regression)
print('gender_f: %.3f' % (f))


# In[291]:


f = evaluate_algorithm(dataset1, simple_linear_regression)
print('f: %.3f' % (f))


# In[ ]:




