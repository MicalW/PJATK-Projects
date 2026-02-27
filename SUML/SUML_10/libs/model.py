import numpy as np
import pandas as pd
import pickle
from sklearn.linear_model import LinearRegression

def predict_Y(x_value, model = 'our_model.pkl'):
    imported_model = pickle.load(open(model, 'rb'))
    X = np.array(x_value)
    X = X.reshape(1, -1)
    y = imported_model.predict(X)

    print('X = ', X[0])
    print('Y = ', y[0][0])
    print('Y poprzez wyliczenie ', imported_model.coef_[0][0]*X[0]+imported_model.intercept_[0])

def new_data(x_value, y_value, path = 'data/10_points.csv', model = 'our_model.pkl'):
    x = x_value
    y = y_value
    df = pd.read_csv(path)
    print('2 ostatnie linie przed dodaniem danych', df.tail(2))
    df.loc[len(df.index)] = [x, y]
    df.to_csv('10_points.csv', index=False)
    df = pd.read_csv('10_points.csv')
    print('2 ostatnie linie po dodaniu danych', df.tail(2))
    X = df['x'].values.reshape(-1, 1)
    y = df['y'].values.reshape(-1, 1)

    our_model = LinearRegression()
    our_model.fit(X, y)

    print('a =', our_model.coef_[0][0])
    print('b =', our_model.intercept_[0])

    pickle.dump(our_model, open(model, 'wb'))

#1 skrypt
predict_Y(2.5, "C:\\Users\\s27568\\PycharmProjects\\JupyterProject\\ml_models\\our_model.pkl")

print('----------------------------')
#2 skrypt
new_data(5, 2.5, "C:\\Users\\s27568\\PycharmProjects\\JupyterProject\\data\\10_points.csv","C:\\Users\\s27568\\PycharmProjects\\JupyterProject\\ml_models\\our_model.pkl")


