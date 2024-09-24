Disponible en: https://wcontact.com.mx/PoderJudicial-21

Obtener listado de boletines por juzgado y fecha:

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/forean_date?judged=FNI&date=2024-09-19&url=bulletin/forean_date
- zmg_date?judged=FNI&date=2024-09-19&url=bulletin/forean_date

- Sistema:https://api.cjj.gob.mx/bulletin/forean_date?judged=FNI&date=2024-09-19&url=bulletin/forean_date

Obtener todo lo mercantil:

https://api.wcontact.loc:8443/PoderJudicial-21/rest/admin/refactorME


HTTP QUERIES PARA OBTENER JUZGADOS formato de respuesta:

```JSON
{
    "success": 1,
    "data": [
        {
        "value": "L03",
        "name": "JUZGADO PRIMERO LABORAL DE LA PRIMERA REGION",
        "secretary": "ALEJANDRA PÉREZ SÁNCHEZ",
        "position": "SECRETARIO INSTRUCTOR",
        "address": "AV. JUAN GIL PRECIADO  NÚMERO 6735, COLONIA NUEVO MÉXICO, C.P. 45138"
        }
    ]
}
```

### ZONA METROPOLITANA:

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/zm_judges?url=bulletin/zm_judges
- SISTEMA: https://api.wcontact.loc:8443/PoderJudicial-21/rest/judge/runquery/zm_judges

### FORANEOS:

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/foraneos_judges?url=bulletin/foraneos_judges
- SISTEMA: https://api.wcontact.loc:8443/PoderJudicial-21/rest/judge/runquery/foraneos_judges

### PUENTE GRANDE:

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/puente_grande_judges?url=bulletin/puente_grande_judges
- SISTEMA: https://api.wcontact.loc:8443/PoderJudicial-21/rest/judge/runquery/puente_grande_judges

### JUICIOS ORALES;

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/penal_oral_judges?url=bulletin/penal_oral_judges
- SISTEMA: https://api.wcontact.loc:8443/PoderJudicial-21/rest/judge/runquery/penal_oral_judges

### JUZGADOS LABORALES:

- PODER JUDICIAL: https://api.cjj.gob.mx/bulletin/labor_judges?url=bulletin/labor_judges
- SISTEMA: https://api.wcontact.loc:8443/PoderJudicial-21/rest/judge/runquery/labor_judges

## GITEE Command line instructions

You can also upload existing files from your computer using the instructions below.

### Git global setup

- git config --global user.name "Luis Longoria"
- git config --global user.email "llongoria@wcontact.com.mx"

### Create a new repository

- git clone https://llongoria@gitee.wcontact.com.mx/web-contact/poderjudicial/backend.git
- cd backend
- git switch --create main
- touch README.md
- git add README.md
- git commit -m "add README"
- git push --set-upstream origin main

### Push an existing folder

- cd existing_folder
- git init --initial-branch=main
- git remote add origin https://llongoria@gitee.wcontact.com.mx/web-contact/poderjudicial/backend.git
- git add .
- git commit -m "Initial commit"
- git push --set-upstream origin main

### Push an existing Git repository

- cd existing_repo
- git remote rename origin old-origin
- git remote add origin https://llongoria@gitee.wcontact.com.mx/web-contact/poderjudicial/backend.git
- git push --set-upstream origin --all
- git push --set-upstream origin --tags