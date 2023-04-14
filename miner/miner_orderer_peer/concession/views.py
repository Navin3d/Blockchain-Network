from rest_framework.views import APIView
from configurations import eureka, kafka, general
from rest_framework.response import Response
from json import dumps
import tensorflow as tf
import os


eureka.register()

ml_url = os.path.abspath(os.getcwd()) + general.ML_MODEL_DIRECTORY
loaded_model = tf.keras.models.load_model(ml_url)


class Transaction(APIView):
    def post(self, request):
        data = request.data

        order = loaded_model.predict([data["data"]])
        data["order"] = float(order[0][0])

        kafka.publish_message("PROCESSED", "PYTHON", dumps(data))
        return Response(status=200, data=data)
