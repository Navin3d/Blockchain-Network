print('Start #################################################################');

db = db.getSiblingDB('own-blockchain');
db.createUser(
  {
    user: 'root',
    pwd: 'root',
    roles: [{ role: 'readWrite', db: 'own-blockchain' }],
  },
);
db.createCollection('users');

print('END #################################################################');
