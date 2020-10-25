# TesisMaestria
Este es el repositorio de la Tesis de Maestría de Miguel Ángel Askar Rodríguez, titulada "Implementación de un algoritmo evolutivo para apoyar la detección de riesgos cardiovasculares a partir del análisis de datos clínicos".

Para probar la ejecución se recomienda cargar el proyecto de la carpeta "PrototipoEvolRed" en Netbeans y ejecutarlo con la versión 8 de java. Igualmente, se anexa una carpeta "Ejecutable", sin embargo, no se recomienda ya que la versión actual de java bloquea la aplicación por no tener un certificado emitido por una autoridad de confianza.

Este es el contenido del respositorio:

Ejecutable (No recomendado):
	Contiene los archivos compilados del prototipo que permite cargar CSV para la predicción de Riesgos Cardiovasculares (RCV).

Entrenamiento AG:Contiene lo siguiente.

	->AgCeros: Contiene el código del algoritmo genético utilizado para entrenarse con los casos de pacientes sanos, así mismo, contiene los 		resultados del entrenamiento.

	->AgUnos: Contiene el código del algoritmo genético utilizado para entrenarse con los casos de pacientes con RCV, así mismo, contiene 		los resultados del entrenamiento.

PrototipoEvolRed:
	Contiene el proyecto de Netbeans con la GUI hecha en JavaFx para cargar y ejecutar el prototipo para la predicción de RCV con el AG y la RNA. Dentro del SRC del proyecto, se encuentra una carpeta llamada "datasets", donde podrá encontrar los datasets de prueba y entrenamiento del algoritmo para poder cargarlos en el prototipo.	

Red Neuronal: Contiene lo siguiente.
	
	->Ejecución sin GUI: contiene un proyecto de Netbeans que ejecuta la red neuronal con el dataset de prueba sin necesidad de una GUI.
	
	->Errores generales del entrenamiento: Contiene todas las imágenes de los errores de entrenamiento de las diferentes RNA candidatas.

	->Proyecto de Neuroph: Contiene el proyecto de Neuroph con la RNA seleccionada.

	->Red iteración adicional: Contiene la red neuronal producto de la iteración adicional.

	->Redes primera iteración: Contiene las redes neuronales producto de la primera iteración.

	->Redes segunda iteración: Contiene las redes neuronales producto de la segunda iteración.

	->Redes tercera iteración: Contiene las redes neuronales producto de la tercera iteración.

Validación Cruzada: Contiene lo siguiente.

	-> Código fuente AG: contiene el código fuente del AG para las 8 ejecuciones.
	
	-> Error general RNA Entrenamiento: contiene los errores al entrenar la RNA en las 4 configuraciones de dataset determinadas. Están nombrado: It<#iteración>-<#ConfiguraciónDataset>

	-> Resultados entrenamiento: contiene los datos de entrenamiento de los 4 datasets, las 4 soluciones del AG resultantes y las 4 RNA resultantes.

	-> MatricesConfusionCruzada.JPG: una imagen de las matrices de confusión resultantes de la validación cruzada.
		
Además, se incluye un archivo comprimido llamado heart-disease-uci.zip, el cual contiene el dataset utilizado para entrenar y probar el AG y la RNA, el cual es tomado de: https://www.kaggle.com/ronitf/heart-disease-uci
