import bs4 as bs
from apscheduler.schedulers.blocking import BlockingScheduler
#sauce = Request('https://www.webmd.com/health-insurance/news/20190307/hidden-fda-reports-show-harm-from-medical-devices#1', headers={'User-Agest': 'Mozilla/5.0'})

#webpage = urlopen(sauce).read()
import requests
import urllib.request

class AppURLopener(urllib.request.FancyURLopener):
    version = "Mozilla/5.0"


#class for the articles 
class Article:     
    articleName = ''
    link = ''
    def __init__(self, articleName, link):
        self.articleName = articleName
        self.link = link
#makes an article and returns it
def make_article(articleName, link):
    article = Article(articleName, link)
    return article
#removes duplicate articles
def remove_duplicates(l):
    return list(set(l))
#gets all articles information and returns list of objects type article
def getArticleInfo(links):
    opener = AppURLopener()
    articles = []
    for link in links:
        response = opener.open(link)
        

        soup = bs.BeautifulSoup(response,'lxml')

        body = soup.body
        title = soup.title
        #found = False
        articles.append(make_article(title.text, link))          
        #for paragraph in body.find_all('p'):
            #if len(paragraph) >=25 and not(found):
         #       print("paragraph " ,paragraph.text)
        #      found = True
                
        #subject = soup.subject

        #print(subject.text)
        #print("new article")
    return articles
#gets all links that are articles on the source given and returns a list of the links 
def getLinks(sourcelink):
    res = requests.get(sourcelink)
    #print(res.text)
    soup = bs.BeautifulSoup(res.text, 'lxml')
    links = []
    for link in soup.find_all('a', href=True, text =True):
        #print (link['href'])
        if not (link['href'].find('news') == -1) and not (link['href'].endswith('default.htm')) and not (link['href'].endswith('news/articles')) and not (link['href'].endswith('aspx')):
            if not link['href'].startswith('#') and (link['href'].find('http') == -1):
                links.append('https:' + link["href"])
                #print('https:' + link["href"])
                
            elif not link['href'].startswith('#') and link['href'].find('https:') != -1 :
                links.append(link["href"])
                #print(link["href"])
    return getArticleInfo(remove_duplicates(links))

def main():
    file = open("links.txt","a")
    links = getLinks('https://www.webmd.com/news/articles') # returns the list of objects Articles
    for link in links:
        file.write(link.link+ "\n")
        file.write(link.articleName+ "\n")
    file.write("searched website one \n")
    links2 = getLinks('https://www.healthline.com/health-news') # returns the list of objects Articles
    for linka in links2:
        file.write(linka.link+ "\n")
        file.write(linka.articleName+ "\n")
    file.write("searched website two\n")
    file.close()
#makes the crawler run every 24 hours
if __name__=="__main__":
    main()
    scheduler = BlockingScheduler()
    scheduler.add_job(main, 'interval', hours=24)
    scheduler.start()





