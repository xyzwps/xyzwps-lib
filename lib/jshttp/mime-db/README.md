
Based on `mime-db` 1.52.0

`db.tsv` generating script:

```js
const db = require('mime-db/db.json');
const _ = require('lodash');

for (const name in db) {
  const meta = db[name];

  const source = _.isEmpty(meta.source) ? '' : `${meta.source.toUpperCase()}`;
  const charset = _.isEmpty(meta.charset) ? '' : `${meta.charset}`;
  const compressible = _.isNil(meta.compressible) ? false : meta.compressible;
  const extensions = _(meta.extensions).join(', ');

  console.log(`${name}\t${source}\t${charset}\t${compressible}\t${extensions}`);
}
```