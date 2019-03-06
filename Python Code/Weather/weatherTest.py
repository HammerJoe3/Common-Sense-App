"""
Weather API Python sample code
Copyright 2019 Oath Inc. Licensed under the terms of the zLib license see https://opensource.org/licenses/Zlib for terms.
$ python --version
Python 2.7.10
"""
import time, uuid, urllib
import hmac, hashlib
from base64 import b64encode
import urllib.parse

"""
Basic info
"""
url = 'https://weather-ydn-yql.media.yahoo.com/forecastrss'
method = 'GET'
app_id = '3DVJdt4e'
consumer_key = 'dj0yJmk9MXd1RGdKUEdJS1hGJnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PWE1'
consumer_secret = '9d546195142fcba8825b2fa773f057a6bedbce29'
concat = '&'
query = {'location': 'sunnyvale,ca', 'format': 'json'}
oauth = {
    'oauth_consumer_key': consumer_key,
    'oauth_nonce': uuid.uuid4().hex,
    'oauth_signature_method': 'HMAC-SHA1',
    'oauth_timestamp': str(int(time.time())),
    'oauth_version': '1.0'
}

"""
Prepare signature string (merge all params and SORT them)
"""
merged_params = query.copy()
merged_params.update(oauth)
sorted_params = [k + '=' + urllib.parse.quote(merged_params[k], safe='') for k in sorted(merged_params.keys())]
signature_base_str =  method + concat + urllib.parse.quote(url, safe='') + concat + urllib.parse.quote(concat.join(sorted_params), safe='')

"""
Generate signature
"""
composite_key = urllib.parse.quote(consumer_secret, safe='') + concat
oauth_signature = b64encode(hmac.new(composite_key, signature_base_str, hashlib.sha1).digest())

"""
Prepare Authorization header
"""
oauth['oauth_signature'] = oauth_signature
auth_header = 'OAuth ' + ', '.join(['{}="{}"'.format(k,v) for k,v in oauth.items()])

"""
Send request
"""
url = url + '?' + urllib.parse.urlencode(query)
request = urllib.request.Request(url)
request.add_header('Authorization', auth_header)
request.add_header('X-Yahoo-App-Id', app_id)
response = urllib.request.urlopen(request).read()
print(response)