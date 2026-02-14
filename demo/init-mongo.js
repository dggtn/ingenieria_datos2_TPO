db.createCollection('calificaciones');
db.createCollection('estudiantes');
db.createCollection('instituciones');
db.createUser({
  user: 'app_user',
  pwd: 'app_password',
  roles: [{ role: 'readWrite', db: 'edugrade' }]
});
