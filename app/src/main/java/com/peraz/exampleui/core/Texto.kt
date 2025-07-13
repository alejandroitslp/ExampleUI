

//Review code to make some things visible to the admin users.

/* La aplicacion tiene una base de datos local, se necesita que cuando se recupere la base de datos
De retrofit, se compare con la base de datos local y todas aquellas no coincidencias se eliminen.

Para esto: Se necesita que se analice por Id, si el Id de la base de datos de room no se
encuentra en la base de datos de retrofit, esta se elimine de room junto con su imagen.
 */