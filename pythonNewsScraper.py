import bs4 as bs

#sauce = Request('https://www.webmd.com/health-insurance/news/20190307/hidden-fda-reports-show-harm-from-medical-devices#1', headers={'User-Agest': 'Mozilla/5.0'})

#webpage = urlopen(sauce).read()

import urllib.request

class AppURLopener(urllib.request.FancyURLopener):
    version = "Mozilla/5.0"

opener = AppURLopener()
response = opener.open('https://www.webmd.com/health-insurance/news/20190307/hidden-fda-reports-show-harm-from-medical-devices#1')


soup = bs.BeautifulSoup(response,'lxml')


body = soup.body
for paragraph in body.find_all('p'):
    print(paragraph.text)
