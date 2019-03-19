
import bs4 as bs

#sauce = Request('https://www.webmd.com/health-insurance/news/20190307/hidden-fda-reports-show-harm-from-medical-devices#1', headers={'User-Agest': 'Mozilla/5.0'})

#webpage = urlopen(sauce).read()
import requests
import urllib.request

class AppURLopener(urllib.request.FancyURLopener):
    version = "Mozilla/5.0"


def getArticleInfo(links):
    opener = AppURLopener()
    for link in links:
        response = opener.open(link)


        soup = bs.BeautifulSoup(response,'lxml')

        body = soup.body
        title = soup.title
        found = False
        print(title.text)
        for paragraph in body.find_all('p'):
            if(len(paragraph) >=25 and not(found)):
                print("paragraph " ,paragraph.text)
                found = True
                break
        #subject = soup.subject

        #print(subject.text)
        print("new article")
    return

def getLinks():
    res = requests.get('https://www.webmd.com/news/default.htm')
    #print(res.text)
    soup = bs.BeautifulSoup(res.text, 'lxml')
    links = []
    for link in soup.find_all('a', href=True, text =True):
        if not (link['href'].find('news') == -1) and not (link['href'].endswith('default.htm')) and not (link['href'].endswith('news/articles')) and not (link['href'].endswith('aspx')):
            if not link['href'].startswith('#') and (link['href'].find('http') == -1):
                links.append('https:' + link["href"])
                print('https:' + link["href"])
            elif not link['href'].startswith('#') and link['href'].find('https:') != -1 :
                links.append(link["href"])
                print(link["href"])
    getArticleInfo(links)
    return

if __name__=="__main__":
    getLinks()
    print("done")
