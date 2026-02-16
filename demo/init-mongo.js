db.createCollection('calificaciones');
db.createCollection('estudiantes');
db.createCollection('institucion');
db.createUser({
  user: 'app_user',
  pwd: 'app_password',
  roles: [{ role: 'readWrite', db: 'edugrade' }]
});
