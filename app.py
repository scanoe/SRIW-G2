# -*- coding: utf-8 -*-
from flask import Flask, request, jsonify
import json


app = Flask(__name__)

@app.before_request
def option_autoreply():
    """ Always reply 200 on OPTIONS request """

    if request.method == 'OPTIONS':
        resp = app.make_default_options_response()

        headers = None
        if 'ACCESS_CONTROL_REQUEST_HEADERS' in request.headers:
            headers = request.headers['ACCESS_CONTROL_REQUEST_HEADERS']

        h = resp.headers

        # Allow the origin which made the XHR
        h['Access-Control-Allow-Origin'] = request.headers['Origin']
        # Allow the actual method
        h['Access-Control-Allow-Methods'] = request.headers['Access-Control-Request-Method']
        # Allow for 10 seconds
        h['Access-Control-Max-Age'] = "1000"

        # We also keep current headers
        if headers is not None:
            h['Access-Control-Allow-Headers'] = headers

        return resp


@app.after_request
def set_allow_origin(resp):
    """ Set origin for GET, POST, PUT, DELETE requests """

    h = resp.headers

    # Allow crossdomain for other HTTP Verbs
    if request.method != 'OPTIONS' and 'Origin' in request.headers:
        h['Access-Control-Allow-Origin'] = request.headers['Origin']
    return resp


@app.route('/companies', methods=['GET'])
def actores():
    if request.method == 'GET':
        q = request.args.get('q')
        act_, status = ac.get_actores(db, q)
        response = app.response_class(
            response=dumps_({"items": list(act_)}),
            status=status,
            mimetype='application/json'
        )
        return response




# Step0
@app.route('/step0', methods=['POST', 'GET'])
def step0():
    if request.method == 'POST':
        sectores = request.json
        """
        conn = pymysql.connect(host='127.0.0.1', user='root', passwd='Lantiasas@2016', db='api_lantia')
        cur = conn.cursor(pymysql.cursors.DictCursor)
        cur.execute('SELECT * FROM portales')
        for r in cur:
            print(r)
        cur.close()
        conn.close()        
        """

        db.sectores.update(
            {'id': 1}, {"$set": {"sectores": sectores}}, upsert=False)

        response = app.response_class(
            response=json.dumps({"sectores": sectores['sectores']}),
            status=201,
            mimetype='application/json'
        )
        return response



if __name__ == '__main__':
    app.secret_key = 'secret1234'
    app.run(host='0.0.0.0', debug=True, port=8080)
