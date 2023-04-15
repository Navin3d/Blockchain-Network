import py_eureka_client.eureka_client as eureka_client
from . import general


def register():
    your_rest_server_port = int(general.PORT)
    eureka_host = f"http://{general.EUREKA_HOST}:8010/eureka"
    print("=====>" + eureka_host)
    eureka_client.init(eureka_server=eureka_host,
                       app_name="Mining-Orderer-Peer",
                       instance_host=general.HOST,
                       instance_port=your_rest_server_port)
