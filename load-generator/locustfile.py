import time
from locust import HttpUser, task, between
from random import randint

class ShopstarTestingUser(HttpUser):
    wait_time = between(1, 5)

    @task(3)
    def get_user(self):
        user_ids = [1, 1710589594401443840]
        for user_id in user_ids:
            self.client.get(f"/v2/users/{user_id}", name="/userDetail")
            time.sleep(1)

    @task(3)
    def get_customer(self):
        user_ids = [1, 1710589594401443840]
        for user_id in user_ids:
            self.client.get(f"/v2/shops/1/customers/by_user?userId={user_id}", name="/customerByUserDetail")
            time.sleep(1)

    @task(2)
    def get_product(self):
        for product_id in range(1,5):
            self.client.get(f"/v2/products/{product_id}", name="/productDetail")
            time.sleep(1)

    @task
    def place_order(self):
        user_ids = [1, 1710589594401443840]
        for user_id in user_ids:
            items = []
            for i in range(3):
                product_id = randint(1,5)
                product_count = randint(1, 30)
                items.append({"productId":product_id, "count":product_count})
            with self.client.post(f"/v2/shops/1/orders_simulator?userId={user_id}", json={"items": items}, name="/placeOrder") as response:
                print(response.text)
                result = response.json()
                order_id = result.get("id")
                if(not order_id is None):
                    i = randint(1, 10)
                    if (i > 2):
                        order_number = result.get("orderNumber")
                        cash_fee = result.get("totalAmountFen")
                        with self.client.put(f"/v2/shops/1/orders_simulator/{order_id}/wxpay_simulator?orderNumber={order_number}&cashFee={cash_fee}", name="/simulateWxPayOrder") as response1:
                            time.sleep(3)
                            self.client.put(f"/v2/shops/1/orders_simulator/{order_id}/confirmation_simulator", name="/simulateConfirmOrder")
            time.sleep(1)

    def on_start(self):
        pass