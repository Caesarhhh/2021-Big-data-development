import numpy as np
import pandas as pd
from pandas import DataFrame
from sklearn.model_selection import train_test_split
from matplotlib import pyplot as plt
from sklearn.metrics import r2_score
from sklearn import ensemble
from sklearn import linear_model
import seaborn as sns
model_LinearRegression = linear_model.LinearRegression()
model_RandomForestRegressor = ensemble.RandomForestRegressor(n_estimators=40)
model=model_LinearRegression

def showEda(df):
    print("基本信息")
    print(df)
    print("空数据信息")
    print(df.isnull().sum())
    print("数字信息")
    print(df.describe(include=np.number))
    print("非数字信息")
    print(df.describe(include=object))

def load_data(df):
    x = df.drop(['date', 'price', 'city', 'street', 'statezip', 'country'], axis=1)
    y = df['price']
    trainx, testx, trainy, testy = train_test_split(x, y, test_size=0.3)
    trainy = np.log1p(trainy)
    return trainx, trainy, testx, testy

def getShowNumSns(data):
    num_var = data.select_dtypes(include=np.number).columns.to_list()
    i = 1
    plt.figure(figsize=(15, 20))
    data1=data.copy()
    for j in num_var:
        plt.subplot(5, 3, i)
        sns.boxplot(data=data1, x=j)
        i += 1
    plt.title("show numvar")
    plt.show()
    return num_var

def showHist(data):
    data1=data.copy()
    data1.hist(figsize=(15, 20))
    plt.title("hist")
    plt.show()

def outlier_tret(x):
    upper=x.quantile(0.98)
    lower=x.quantile(0.2)
    x=np.where(x>upper,upper,x)
    x=np.where(x<lower,lower,x)
    return x

def relaAna(data):
    data1=data.copy()
    plt.figure(figsize=(10, 10))
    sns.heatmap(data=data1.drop(columns='waterfront').corr(), linewidths=1, cmap='coolwarm_r', annot=True)
    plt.title("relation with prices")
    plt.show()

def showCorr():
    corr = data1.corr()["price"].sort_values(ascending=False)
    plt.figure(figsize=(10, 5))
    corr.plot(kind='bar', color='red')
    plt.title("corr with price")
    plt.show()

def train_predict(trainx,trainy,testx,testy):
    model.fit(trainx,trainy)
    pred1=model.predict(testx)
    np_testx=np.array(testx)
    np_testy=np.array(testy)
    np_predy=np.array(pred1)
    np_predy=np.exp(np_predy)-1
    data={
        "num":np.array(range(testx.shape[0])),
        "actual":np_testy,
        "predict":np_predy
    }
    dfy=DataFrame(data)
    dfy.to_csv("./result.csv")
    r2s=r2_score(np_testy,np_predy)
    num=np_testx.shape[0]
    plt.figure(figsize=(15, 5))
    plt.plot(np.linspace(1.0,num,num=num),np_predy,color='green',label="predict")
    plt.plot(np.linspace(1.0,num,num=num),np_testy,color='blue',label="actual")
    plt.title("Actual Price VS Predicted Price")
    plt.legend()
    plt.show()
    print("r2s="+str(r2s))

if __name__ == "__main__":
    df = pd.read_csv("./dataset/data.csv")
    showEda(df)
    data1=df.copy()
    num_var=getShowNumSns(data1)
    data1[num_var] = data1[num_var].apply(lambda x: outlier_tret(x))
    getShowNumSns(data1)
    showHist(data1)
    relaAna(data1)
    showCorr()
    trainx,trainy,testx,testy=load_data(data1)
    train_predict(trainx,trainy,testx,testy)


