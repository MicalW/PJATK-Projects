import pickle
from skl2onnx import convert_sklearn
from skl2onnx.common.data_types import FloatTensorType

initial_type = [
    ('x', FloatTensorType([None, 1]))
]

model = pickle.load(open("ml_models/our_model.pkl", "rb"))
converted_model = convert_sklearn(model, initial_types=initial_type)
with open("ml_models/our_model.onnx", "wb") as f:
    f.write(converted_model.SerializeToString())

//python -m onnxruntime.tools.convert_onnx_models_to_ort C:\Users\s27568\PyCharmMiscProject\ml_models\our_model.onnx
//pip install skl2onnx scikit-learn onnxruntime
