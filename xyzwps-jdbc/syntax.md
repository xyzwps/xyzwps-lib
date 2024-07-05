```
() : group
[] : optional
|  : or

sqlMethod := 
       | find_method
       | count_method
       | update_method
       | delete_method 

find_method   := ("find" | "get") [condition] [orderByPart] [limitPart]
count_method  := "count"  [condition]
update_method := "update" setPart [condition]
delete_method := "delete" [condition]

condition := ("by" | "where") conditionExp

conditionExp := booleanExp | andExp | orExp
andExp       := booleanExp "and" conditionExp
orExp        := booleanExp "or"  conditionExp
booleanExp :=
          | eqExp | neExp | gtExp | geExp | ltExp | leExp
          | likeExp       | notLikeExp
          | inExp         | notInExp
          | betweenAndExp | notBetweenAndExp
          | isNullExp     | isNotNullExp

eqExp            := column ["eq"]
neExp            := column "ne"
gtExp            := column "gt"
geExp            := column "ge"
ltExp            := column "lt"
leExp            := column "le"
likeExp          := column "like"
notLikeExp       := column "not" "like"
inExp            := column "in"
notInExp         := column "not" "in"
betweenAndExp    := column "between" "and"
notBetweenAndExp := column "not" "between" "and"
isNullExp        := column ["is"] "null"
isNotNullExp     := column ["is"] "not" "null"

column := columnNameSegment [column]

orderByPart   := "order" "by" orderByList
orderByList   := orderByColumn [orderByList]
orderByColumn := column ["asc" | "desc"]

limitPart := "limit"

setPart := "set" setList
setList := column ["and" setList]
```