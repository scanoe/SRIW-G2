# -*- coding: utf-8 -*-
"""
Created on Sat May 26 10:47:14 2018

@author: sebastian
"""

Likes={"ips":[{"id":"1234","municipio":"A","departamento":"z","like":1,"Servicio":"CONSULTAS"},{"id":"1233","municipio":"B","departamento":"t","like":0,"Servicio":"URGENCIAS"},{"id":"1239","municipio":"C","departamento":"z","like":1,"Servicio":"URGENCIAS"}]}

municipiosLike ={"A":0,"B":0,"C":0}
municipiosNoLike ={"A":0,"B":0,"C":0}
probLikeMuni={"A":0,"B":0,"C":0}
proNobLikeMuni={"A":0,"B":0,"C":0}
ProbTotalesMuni={"A":0,"B":0,"C":0}


departamentosLike={"z":0,"t":0,"c":0}
departamentosNoLike={"z":0,"t":0,"c":0}
probLikeDepto={"z":0,"t":0,"c":0}
probNoLikeDepto={"z":0,"t":0,"c":0}
probTotalesDpto={"z":0,"t":0,"c":0}



serviciosLike={"HOSPITALARIA":0,"CONSULTAS":0,"EXPERIENCIA GLOBAL":0,"URGENCIAS":0}
serviciosNoLike={"HOSPITALARIA":0,"CONSULTAS":0,"EXPERIENCIA GLOBAL":0,"URGENCIAS":0}
probLikeServ={"HOSPITALARIA":0,"CONSULTAS":0,"EXPERIENCIA GLOBAL":0,"URGENCIAS":0}
probNoLikeServ={"HOSPITALARIA":0,"CONSULTAS":0,"EXPERIENCIA GLOBAL":0,"URGENCIAS":0}
probTotalesServ={"HOSPITALARIA":0,"CONSULTAS":0,"EXPERIENCIA GLOBAL":0,"URGENCIAS":0}



ips={"ips":[{"id":"1234","municipio":"A","departamento":"z","Servicio":"CONSULTAS"},{"id":"1233","municipio":"B","departamento":"t","Servicio":"URGENCIAS"}
,{"id":"1235","municipio":"C","departamento":"z","Servicio":"CONSULTAS"},{"id":"1237","municipio":"C","departamento":"c","Servicio":"CONSULTAS"}]}

probLike=0.5
recomendacion={}




Calificaciones=0
contLike=0
contNoLike=0
for i in Likes["ips"] :
    Calificaciones = Calificaciones +1
    
    if i["like"]==1 :
        contLike=contLike+1
        serviciosLike[i["Servicio"]]=serviciosLike[i["Servicio"]]+1
        municipiosLike[i["municipio"]]=municipiosLike[i["municipio"]]+1
        departamentosLike[i["departamento"]]=departamentosLike[i["departamento"]]+1

    if i["like"]==0 :
        contNoLike=contNoLike+1
        serviciosNoLike[i["Servicio"]]=serviciosNoLike[i["Servicio"]]+1
        municipiosNoLike[i["municipio"]]=municipiosNoLike[i["municipio"]]+1
        departamentosNoLike[i["departamento"]]=departamentosNoLike[i["departamento"]]+1
        

# saca las probabilidades condicionadas de like para cada feuture
for serv in probLikeServ.keys():
    
    if serviciosLike[serv]==0:
        probLikeServ[serv]=0.001
    else:
        probLikeServ[serv]=serviciosLike[serv]/contLike
        
        
for dep in probLikeDepto.keys():
    
    if departamentosLike[dep]==0:
        probLikeDepto[dep]=0.001
    else:
        probLikeDepto[dep]=departamentosLike[dep]/contLike

for muni in probLikeMuni.keys():
    
    if municipiosLike[muni]==0:
        probLikeMuni[muni]=0.001
    else:
        probLikeMuni[muni]=municipiosLike[muni]/contLike    
        

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
         probTotalesServ[serv]=(serviciosLike[serv]+serviciosNoLike[serv])/Calificaciones
         
         
for dep in probTotalesDpto.keys():
    if (departamentosLike[dep]+departamentosNoLike[dep])==0:
        probTotalesDpto[dep]=0.001
    else:
         probTotalesDpto[dep]=(departamentosLike[dep]+departamentosNoLike[dep])/Calificaciones


for muni in ProbTotalesMuni.keys():
    if (municipiosLike[muni]+municipiosNoLike[muni])==0:
        ProbTotalesMuni[muni]=0.001
    else:
         ProbTotalesMuni[muni]=(municipiosLike[muni]+municipiosNoLike[muni])/Calificaciones
         
         
# se hace el calculo de probabilidad que le guste
         
for ips in ips["ips"]:
    ID=ips["id"]
    servicio=ips["Servicio"]
    municipio=ips["municipio"]
    departamento=ips["departamento"]
    print(ID)
    print(probLikeServ[servicio])
    print(probLikeDepto[departamento])
    print(probLikeMuni[municipio])
    print(probTotalesServ[servicio])
    print(probTotalesDpto[departamento])
    print(ProbTotalesMuni[municipio])
    
    
    prob=(probLike*probLikeServ[servicio]*probLikeDepto[departamento]*probLikeMuni[municipio])/(probTotalesServ[servicio]*probTotalesDpto[departamento]*ProbTotalesMuni[municipio])
    
    if ID in recomendacion:
        if prob> recomendacion[ID]:
            recomendacion[ID]=prob
    else :
        recomendacion[ID]=prob
        

        
print("holi")
print(Calificaciones)
print(contLike)

print(contNoLike)
print(recomendacion)
    
         
print("probabilidades totales")

print(probTotalesServ)
print(probTotalesDpto)
print(ProbTotalesMuni)     
        
        
print("prob no like") 
print(probLikeServ)
print(probLikeDepto)
print(probLikeMuni)  
print("probLike")    
print(probLikeServ)
print(probLikeDepto)
print(probLikeMuni)       
    
print("contadores")
        
print(serviciosLike)
print(serviciosNoLike)
print(departamentosLike)
print(departamentosNoLike)
print(municipiosLike)
print(municipiosNoLike)

        
    


    
    
    
