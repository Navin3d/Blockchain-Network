from django.urls import path
from .views import Transaction


urlpatterns = [
    path("", Transaction.as_view(), name="transaction_view")
]
