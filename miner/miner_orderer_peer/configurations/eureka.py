import py_eureka_client.eureka_client as eureka_client
from . import general


def register():
    your_rest_server_port = 8000
    eureka_client.init(eureka_server=f"http://{general.HOST}:8010/eureka",
                       app_name="Mining-Orderer-Peer",
                       instance_host=general.HOST,
                       instance_port=your_rest_server_port)
