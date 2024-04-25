Based on [`mime-db`](https://github.com/jshttp/mime-db) 1.52.0

`db.csv` generating script:

```js
const db = require('mime-db/db.json');
const _ = require('lodash');

for (const name in db) {
  const meta = db[name];

  const source = _.isEmpty(meta.source) ? '' : `${meta.source.toUpperCase()}`;
  const charset = _.isEmpty(meta.charset) ? '' : `${meta.charset}`;
  const compressible = _.isNil(meta.compressible) ? false : meta.compressible;
  const extensions = _(meta.extensions).join(';');

  console.log(`${name},${source},${charset},${compressible},${extensions}`);
}
```