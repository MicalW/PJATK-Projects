import mlflow
from mlflow.models import infer_signature

import pandas as pd

from sklearn.ensemble import RandomForestClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score

dsp6 = pd.read_csv("data/DSP_6.csv")
print(dsp6)

X = dsp6.drop(["Survived"], axis=1)
y = dsp6["Survived"]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.1, random_state=42)
params_forest = {
    "n_estimators": 10,
    "random_state": 42
}
params_lreg = {
    "max_iter": 500,
    "random_state": 42,
    "solver": "lbfgs"
}

#trening
forest = RandomForestClassifier(**params_forest)
forest.fit(X_train, y_train)
score_forest = forest.score(X_test, y_test)

lreg = LogisticRegression(**params_lreg)
lreg.fit(X_train, y_train)
score_lreg = lreg.score(X_test, y_test)

tree = DecisionTreeClassifier()
tree.fit(X_train, y_train)
score_tree = tree.score(X_test, y_test)

#predykcja
y1_predict = forest.predict(X_test)
y2_predict = lreg.predict(X_test)
y3_predict = tree.predict(X_test)

#metryki
acc_forest = accuracy_score(y_test, y1_predict)
acc_lreg = accuracy_score(y_test, y2_predict)
acc_tree = accuracy_score(y_test, y3_predict)

# ustawienia adresu serwera
mlflow.set_tracking_uri("http://127.0.0.1:8080")

#ustawienie eksperymentu
mlflow.set_experiment("MLFlow Titanic")

#Uruchomienie Mlflow
with mlflow.start_run():
    # Rejestracja hiper parametrów
    mlflow.log_param(params_forest)
    mlflow.log_param(params_lreg)

    #Rejestracja acc
    mlflow.log_metric("acc_forest", acc_forest)
    mlflow.log_metric("acc_lreg", acc_lreg)
    mlflow.log_metric("acc_tree", acc_tree)

    # Ustawienie tagu
    mlflow.set_tag("Trening Info", "Standardowe modele: losowe lasy, regresja logistyczna, drzewa decyzyjne dla danych Titanic")

    #Ustawienie znaczników w celu określenia wyników
    sign_forest = infer_signature(X_train, forest.predict(X_train))
    sign_lreg = infer_signature(X_test, lreg.predict(X_test))
    sign_tree = infer_signature(X_test, tree.predict(X_test))

    #Rejestracja modeli
    model_info_forest = mlflow.sklearn.log_model(
        sk_model = forest,
        artefact_path = "titanic_model_forest",
        signature = sign_forest,
        input_example= X_train,
        registered_model_name="titanic_ml_forest"
    )
    model_info_lreg = mlflow.sklearn.log_model(
        sk_model = lreg,
        artefact_path = "titanic_ml_lreg",
        signature = sign_lreg,
        input_example= X_train,
        registered_model_name="titanic_ml_lreg"
    )
    model_info_tree = mlflow.sklearn.log_model(
        sk_model = tree,
        artefact_path = "titanic_ml_tree",
        signature = sign_tree,
        input_example= X_train,
        registered_model_name="titanic_ml_tree"
    )

    #Załadowanie model
    loaded_model_forest = mlflow.pyfunc.load_model(model_info_forest)
    loaded_model_lreg = mlflow.pyfunc.load_model(model_info_lreg)
    loaded_model_tree = mlflow.pyfunc.load_model(model_info_tree)

    y_pred1 = loaded_model_forest.predict(X_test)
    y_pred2 = loaded_model_lreg.predict(X_test)
    y_pred3 = loaded_model_tree.predict(X_test)

    titanic_acc = ["Random Forest", "Logistic Regression", "Decision Tree"]

    result = pd.DataFrame()
    result[titanic_acc[0]] = y_pred1
    result[titanic_acc[1]] = y_pred2
    result[titanic_acc[2]] = y_pred3

