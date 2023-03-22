from rest_framework.views import APIView
from configurations import eureka, kafka
from rest_framework.response import Response
from json import dumps

eureka.register()


class Transaction(APIView):
    def post(self, request):
        data = request.data
        kafka.publish_message("PROCESSED", "PYTHON", dumps(data))
        return Response(status=200, data=data)
