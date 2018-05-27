# coding: utf-8
from flask import Flask, request, jsonify
import json
import rdflib
from flaskext.mysql import MySQL
from rdflib import Graph
from flask import abort
from flask import make_response

mysql = MySQL()
app = Flask(__name__)
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'SRWgrupo_2'
app.config['MYSQL_DATABASE_DB'] = 'EPS'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)
g = Graph()
g.parse('Datos.owl')


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

@app.route('/usuario/registro', methods=['POST'])
def registro():
    email = request.json['email']
    password = request.json['password']
    conn = mysql.connect()
    cursor = conn.cursor()
    cursor.execute("INSERT INTO usuarios VALUES('" + email + "', '" + password + "')")
    conn.commit()
    conn.close()
    return email + " " + password 

@app.route('/verinfo', methods=['GET'])
def ver_info():
    id = request.args.get('id')
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT *\
            WHERE {\
                ?ips ips:idips ?id;\
                ips:municipio ?mun;\
                ips:departamento ?dep\
                FILTER REGEX(?id, "' + id + '+")'\
            ' }'
    data = dict()
    for row in g.query(query):
        data['id'] = str(row.asdict()['id'])
        data['municipio'] = str(row.asdict()['mun'])
        data['departamento'] = str(row.asdict()['dep'])
        
    query = 'PREFIX ips:<http://www.EPSColombia.org#>\
        SELECT *\
        WHERE {\
            ?ips ips:idips ?id;\
            ips:nomservicio ?ser;\
            FILTER REGEX(?id, "' + id + '+")'\
        ' }'

    servicios = []
    for row in g.query(query):
        servicios.append(str(row.asdict()['ser']))   
    data['servicios'] = servicios

    if len(servicios) == 0:
        abort(404)
    else:
        response = app.response_class(
            response=json.dumps({'ips': data}),
            status=200,
            mimetype='application/json'
        )
        
    return response

@app.route('/ips', methods=['GET'])
def ips():
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT *\
            WHERE {\
                ?i ips:idips ?id;\
                ips:ips ?nom;\
                ips:municipio ?mun;\
                ips:departamento ?dep;\
                ips:resultado ?res\
            }'
    data = []
    for row in g.query(query):
        data2 = dict()
        data2['id'] = str(row.asdict()['id'])
        data2['nombre'] = str(row.asdict()['nom'])
        data2['municipio'] = str(row.asdict()['mun'])
        data2['departamento'] = str(row.asdict()['dep'])
        data2['resultado'] = str(row.asdict()['res'])
        data.append(data2)
    
    response = app.response_class(
        response=json.dumps({'ips': data}),
        status=200,
        mimetype='application/json'
    )
        
    return response        

# Retornar errores como json
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.secret_key = 'secret1234'
    app.run(host='0.0.0.0', debug=True, port=8080)
