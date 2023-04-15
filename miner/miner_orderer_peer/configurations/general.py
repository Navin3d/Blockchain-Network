import os


sep = "/"
ML_MODEL_DIRECTORY = f'{sep}static{sep}ml_models{sep}Miner_Consensus'

EUREKA_HOST = os.environ.get("EUREKA_HOST", default="miner-discovery")
HOST = os.environ.get("HOST", default="127.0.0.1")
PORT = os.environ.get("PORT", default=8000)
