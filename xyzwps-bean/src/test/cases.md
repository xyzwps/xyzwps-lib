## 无继承

大分类如下：

| Type \ AC | private | protected | public | package |
|:---------:|:-------:|:---------:|:------:|:-------:|
| primitive |   C11   |    C12    |  C13   |   C14   |
|  object   |   C21   |    C22    |  C23   |   C24   |
|  generic  |   C31   |    C32    |  C33   |   C34   |
| `boolean` |   C41   |    C42    |  C43   |   C44   |

注：`boolean` 不同于其他 primitive，单独处理。

Case 编号规则：

```text

     CNN    (0|1)   (0|1)   (0|1)
  |-------|-------|-------|-------|
      1       2       3       4
      
    1. 大分类 
    2. 有无对应的 field       0 - 无
    3. 有无对应的 getter      1 - 有
    4. 有无对应的 setter 
```

## 有继承



