import os


sep = "/"
ML_MODEL_DIRECTORY = f'{sep}static{sep}ml_models{sep}Miner_Consensus'

EUREKA_HOST = os.environ.get("EUREKA_HOST", default="localhost")
EUREKA_PORT = int(os.environ.get("EUREKA_PORT", default=8010))

KAFKA_HOST = os.environ.get("KAFKA_HOST", default="localhost")
KAFKA_PORT = int(os.environ.get("KAFKA_PORT", default=9092))

HOST = os.environ.get("HOST", default="127.0.0.1")
PORT = int(os.environ.get("PORT", default=8000))
