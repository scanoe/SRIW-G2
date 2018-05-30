# coding: utf-8
from flask import Flask, request, jsonify
import json
import rdflib
import collections
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
    try:
        cursor.execute("INSERT INTO usuarios VALUES('" + email + "', '" + password + "')")
        conn.commit()
        conn.close()
        response = app.response_class(
            response=json.dumps({'message': 'Registro exitoso'}),
            status=201,
            mimetype='application/json'
        ) 
    except:
       response = app.response_class(
           response = json.dumps({'message':'Registro no exitoso'}),
           status = 401,
           mimetype='apllication/json'
)
    return response


@app.route('/usuario/iniciar-sesion', methods=['POST'])
def iniciar_sesion():
    email = request.json['email']
    password = request.json['password']
    conn = mysql.connect()
    cursor = conn.cursor()
    try:
        cursor.execute("SELECT * FROM usuarios WHERE email='" + email + "' AND password= '" + password + "'")
        data = cursor.fetchall()
        if len(data) == 0:
            response = app.response_class(
                response = json.dumps({'message':'Inicio sesión no exitoso'}),
                status = 401,
                mimetype='apllication/json'
            )
        else:
            response = app.response_class(
                response=json.dumps({'message': 'Inicio sesión exitoso'}),
                status=201,
                mimetype='application/json'
            ) 
        conn.close()
    except:
       response = app.response_class(
           response = json.dumps({'message':'Registro no exitoso'}),
           status = 401,
           mimetype='apllication/json'
       )
    return response


@app.route('/usuario/calificar', methods=['POST'])
def calificar():
    email = request.json['email']
    calificaciones = request.json['calificaciones']
    dic = []
    for c in calificaciones:
        id = c['id']

        query ='PREFIX ips:<http://www.EPSColombia.org#>\
        SELECT *\
        WHERE {\
            ?ips ips:idips ?id;\
            ips:ips ?nom;\
            ips:municipio ?mun;\
            ips:departamento ?dep;\
            ips:nomservicio ?ser\
            FILTER REGEX(?id, "' + id + '+")'\
        ' }'

        data = dict()
        for row in g.query(query):
            data['id'] = str(row.asdict()['id'])
            data['nombre'] = str(row.asdict()['nom'])
            data['municipio'] = str(row.asdict()['mun'])
            data['departamento'] = str(row.asdict()['dep'])
            data['servicio'] = str(row.asdict()['ser'])
            data['like'] = c['like']
        dic.append(data)

        conn = mysql.connect()
        cursor = conn.cursor()
        
        try: 
            cursor.execute("INSERT INTO calificaciones VALUES('" + email + "', '" + data['id'] + "', " + str(c['like']) +")")
            conn.commit()
            conn.close()
            response = app.response_class(
                response=json.dumps({'message': 'Registro exitoso'}),
                status=201,
                mimetype='application/json'
        ) 
        except:
            abort(500)

    Likes = {"ips": dic}
    listadep = get_departamentos()
    listaMunicipips = get_municipios()
    listaderv=["Hospitalaria","Consultas","Experiencia global","Urgencias"]

    ips = get_ips()
    municipiosLike={}
    municipiosNoLike={}
    probLikeMuni={}
    proNobLikeMuni={}
    ProbTotalesMuni={}
    for mun in listaMunicipips:
        municipiosLike[mun]=0
        municipiosNoLike[mun]=0
        probLikeMuni[mun]=0
        proNobLikeMuni[mun]=0
        ProbTotalesMuni[mun]=0
    
        ## se llenan los diccioarios por de departameno
        departamentosLike={}
        departamentosNoLike={}
        probLikeDepto={}
        probNoLikeDepto={}
        probTotalesDpto={} 
    for dep in listadep:
        departamentosLike[dep]=0
        departamentosNoLike[dep]=0
        probLikeDepto[dep]=0
        probNoLikeDepto[dep]=0
        probTotalesDpto[dep]=0
    
    serviciosLike={}
    serviciosNoLike={}
    probLikeServ={}
    probNoLikeServ={}
    probTotalesServ={}  
    for servi in listaderv:
        serviciosLike[servi]=0
        serviciosNoLike[servi]=0
        probLikeServ[servi]=0
        probNoLikeServ[servi]=0
        probTotalesServ[servi]=0
    
    probLike=0.5
    recomendacion={}
    Calificaciones=0
    contLike=0
    contNoLike=0
    for i in Likes["ips"] :
        Calificaciones = Calificaciones +1
        
        if i["like"]==1 :
            contLike=contLike+1
            serviciosLike[i["servicio"]]=serviciosLike[i["servicio"]]+1
            municipiosLike[i["municipio"]]=municipiosLike[i["municipio"]]+1
            departamentosLike[i["departamento"]]=departamentosLike[i["departamento"]]+1

        if i["like"]==0 :
            contNoLike=contNoLike+1
            serviciosNoLike[i["servicio"]]=serviciosNoLike[i["servicio"]]+1
            municipiosNoLike[i["municipio"]]=municipiosNoLike[i["municipio"]]+1
            departamentosNoLike[i["departamento"]]=departamentosNoLike[i["departamento"]]+1
            

    # saca las probabilidades condicionadas de like para cada feuture
    for serv in probLikeServ.keys():
        
        if serviciosLike[serv]==0:
            probLikeServ[serv]=0.001
        else:
            probLikeServ[serv]=serviciosLike[serv]/sum(serviciosLike.values())
            
            
    for dep in probLikeDepto.keys():
        
        if departamentosLike[dep]==0:
            probLikeDepto[dep]=0.001
        else:
            probLikeDepto[dep]=departamentosLike[dep]/sum(departamentosLike.values())

    for muni in probLikeMuni.keys():
        
        if municipiosLike[muni]==0:
            probLikeMuni[muni]=0.001
        else:
            probLikeMuni[muni]=municipiosLike[muni]/sum(municipiosLike.values())    
            

    # saca la probabilidadd condicionada de no like para cada atributo      
        
    for serv in probNoLikeServ.keys():
        
        if serviciosNoLike[serv]==0:
            probNoLikeServ[serv]=0.001
        else:
            probNoLikeServ[serv]=serviciosNoLike[serv]/contNoLike
            
    for dep in probNoLikeDepto.keys():
        
        if departamentosNoLike[dep]==0:
            probNoLikeDepto[dep]=0.001
        else:
            probNoLikeDepto[dep]=departamentosNoLike[dep]/contNoLike

    for muni in proNobLikeMuni.keys():
        
        if municipiosNoLike[muni]==0:
            proNobLikeMuni[muni]=0.001
        else:
            proNobLikeMuni[muni]=municipiosNoLike[muni]/contNoLike   
            
            
    #calcular las probbilidades totales
            
            
    for serv in probTotalesServ.keys():
        if (serviciosLike[serv]+serviciosNoLike[serv])==0:
            probTotalesServ[serv]=0.001
        else:
            probTotalesServ[serv]=(serviciosLike[serv]+serviciosNoLike[serv])/(sum(serviciosLike.values())+sum(serviciosNoLike.values()))
            
            
    for dep in probTotalesDpto.keys():
        if (departamentosLike[dep]+departamentosNoLike[dep])==0:
            probTotalesDpto[dep]=0.001
        else:
            probTotalesDpto[dep]=(departamentosLike[dep]+departamentosNoLike[dep])/(sum(departamentosLike.values())+sum(departamentosNoLike.values()))


    for muni in ProbTotalesMuni.keys():
        if (municipiosLike[muni]+municipiosNoLike[muni])==0:
            ProbTotalesMuni[muni]=0.001
        else:
            ProbTotalesMuni[muni]=(municipiosLike[muni]+municipiosNoLike[muni])/(sum(municipiosLike.values())+sum(municipiosNoLike.values()))
            
            
    # se hace el calculo de probabilidad que le guste
            
    for ips in ips["ips"]:
        ID=ips["id"]
        servicio=ips["servicio"]
        municipio=ips["municipio"]
        departamento=ips["departamento"]    
        prob=(probLike*probLikeServ[servicio]*probLikeDepto[departamento]*probLikeMuni[municipio])/(probTotalesServ[servicio]*probTotalesDpto[departamento]*ProbTotalesMuni[municipio])
        
        if ID in recomendacion:
            if prob> recomendacion[ID]:
                recomendacion[ID]=prob
        else :
            recomendacion[ID]=prob
            

            

    sorrecomendacion=collections.OrderedDict(sorted(recomendacion.items()))
    dic = []
    for id in sorrecomendacion:
        query ='PREFIX ips:<http://www.EPSColombia.org#>\
        SELECT *\
        WHERE {\
            ?ips ips:idips ?id;\
            ips:ips ?nom;\
            FILTER REGEX(?id, "' + id + '+")'\
        ' }'

        data = dict()
        for row in g.query(query):
            data['id'] = str(row.asdict()['id'])
            data['nombre'] = str(row.asdict()['nom'])
        dic.append(data)
    

    response = app.response_class(
        response = json.dumps({'ips': dic}),
        status = 401,
        mimetype='apllication/json'
    )
    return response    

    
@app.route('/verinfo', methods=['GET'])
def ver_info():
    id = request.args.get('id')
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT *\
            WHERE {\
                ?ips ips:idips ?id;\
                ips:ips ?nom;\
                ips:municipio ?mun;\
                ips:departamento ?dep;\
                ips:nomservicio ?ser\
                FILTER REGEX(?id, "' + id + '+")'\
            ' }'
    data = dict()
    for row in g.query(query):
        data['id'] = str(row.asdict()['id'])
        data['nombre'] = str(row.asdict()['nom'])
        data['municipio'] = str(row.asdict()['mun'])
        data['departamento'] = str(row.asdict()['dep'])
        data['servicio'] = str(row.asdict()['ser'])

    if len(data) == 0:
        abort(404)
    else:
        response = app.response_class(
            response=json.dumps({'ips': data}),
            status=200,
            mimetype='application/json'
        )
        
    return response

@app.route('/ips', methods=['GET'])
def get_ips():
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT *\
            WHERE {\
                ?i ips:idips ?id;\
                ips:ips ?nom;\
                ips:nomservicio ?ser;\
                ips:municipio ?mun;\
                ips:departamento ?dep;\
                ips:resultado ?res\
            }'
    data = []
    for row in g.query(query):
        data2 = dict()
        data2['id'] = str(row.asdict()['id'])
        data2['nombre'] = str(row.asdict()['nom'])
        data2['servicio'] = str(row.asdict()['ser'])
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

def get_municipios():
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT DISTINCT ?mun\
            WHERE {\
                ?ips ips:municipio ?mun\
            }'

    data = []
    for row in g.query(query):
        data.append(str(row.asdict()['mun']))
        
    return data

def get_departamentos():
    query ='PREFIX ips:<http://www.EPSColombia.org#>\
            SELECT DISTINCT ?dep\
            WHERE {\
                ?ips ips:departamento ?dep\
            }'

    data = []
    for row in g.query(query):
        data.append(str(row.asdict()['dep']))
      
    return data 

# Retornar errores como json
@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

if __name__ == '__main__':
    app.secret_key = 'secret1234'
    app.run(host='0.0.0.0', debug=True, port=8080)
