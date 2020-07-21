# Life cycle

- vue 인스턴스는 생성될 때 일련의 초기화 과정을 가진다
    - 데이터 관찰
    - 템플릿 컴파일
    - 인스턴스를 dom에 마운트
    - 데이터가 변경되어 dom을 업데이트

## Lifecycle Hooks

- beforeCreate : vue 인스턴스가 생성되고 각 정보의 설정 전에 호출
- created : vue 인스턴스가 생성된 후 데이터들의 설정이 완료 후 호출
- beforeMount : 마운트가 시작되기 전에 호출
- mounted : 지정된 엘리먼트에 vue 인스턴스 데이터가 마운트 된 후에 호출
- beforeUpdate : 데이터가 변경될 때 virtual dom이 랜더링, 패치 되기 전 호출
- updated : vue에서 관리되는 데이터가 변경되어 dom이 업데이트 된 상태
- beforeDestroy : vue instance가 제거되기 전에 호출
- destroyed : vue instance가 제거된 후에 호출

# 문법

## Vue.js 인스턴스

```jsx
const vm = new Vue({
});

```

Vue앱은 Vue 함수로 인스턴스를 만드는 것부터 시작

## Vue.js el 옵션

- **el : css 선택자 or HTML Element, Vue가 적용될 요소 지정**

```jsx
new Vue({
el : "#app"
}):
<div id="app"></div>
```

## data 옵션

- 객체 or 함수, Vue에서 사용되는 정보를 저장

```jsx
new Vue({
  el : "#app",
  data : {
    msg: 'Hello'
  }
});

new Vue({
  el : "#app",
  data() {
    return {
      msg: 'Hello'
    }
  }
});

<div id="app">
  <h2>{{ msg }} </h2>
</div>
```

## 텍스트

- {{ 속성명 }} : 데이터 바인딩의 기본 형태
- v-once : 데이터 변경 시 업데이트 되지 않는 일회성 보간 수행

`<span>메시지 : {{msg}}</span>`

`<span v-once>메시지 : {{msg}}</span>`

## Raw HTML

- {{속성명}}, v-text : 데이터 속성의 html을 escape 처리
- v-html : 데이터 속성의 html을 파싱하여 처리

```jsx
<div>{{msg}}</div>
<div v-text="msg">무시</div>
<div v-html="msg">무시</div>
```

## Javascript Expressions

- 데이터 바인딩 내에서 javascript 표현식의 사용이 가능
- 주의 : 하나의 단일 표현식만 포함될 수 있음

```jsx
{{ number + 1}}
{{ ok? 'yes' : 'no'}}
{{ message.split('-').reverse().join("")}}
```

## Directives

- v-text
- v-html
- v-once
- v-bind
- v-show
- v-if
- v-else-if
- v-else
- v-for
- v-cloak
- v-on

## v-model

- 양방향 바인딩 처리를 위해서 사용(폼의 input, textarea)

```jsx
new Vue({
  el : "#app",
  data : {
    msg : 'hello'
  }
});

<input type= "text" v-model = "msg">
```

## v-bind

- 엘리먼트 속성 바인딩 처리를 위해서 사용
- v-bind는 약어로 “:”로 사용 가능

```jsx
new Vue({
  el : "#app",
  data : {
    idVal : 'text',
    key: 'id'
  }
});

<div v-bind:id="idVal">test</div>
<div v-bind:[key]="idVal">text</div>
<div :id="idVal">test</div>
<div :[key]="idVal">text</div>
```

## v-show

- 조건에 따라 엘리먼트를 화면에 표시
- style 속성의 display를 변경

```jsx
<button v-show="isExist">검색</button>
```

## v-if, v-else, v-else

- 조건에 맞는 경우 화면에 요소들을 랜더링

```jsx
<span v-if"=age < 10">무료</span>
<span v-else-if="age < 20">7000won</span>
<span v-else-if="age < 65">10000won</span>
<span v-else>3000won</span>
```

## v-for

- 배열이나 객체의 반복에 사용
- v-for=”요소변수이름 in 배열” v-for=”(요소변수이름, 인덱스) in 배열”

```jsx
new Vue({
  el : "#app",
  data : {
    regions: ["1", "2", "3", "4"]
  }
});

<ul>
  <li v-for="name in regions">
  지역 : {{name}}
  </li>
</ul>
```

## template

- 여러 개의 태그들을 묶어서 처리해야 한다면 template을 이용하면 편리
- v-if, v-for, component 등과 함께 많이 사용

```jsx
<template v-for"(region, index) in ssafy">
  <h2> 지역 : {{region.name}}</h2>
  <h3>{{region.count}}개반</h3>
</template>
```

## v-cloak

- vue인스턴스가 준비될 때까지 mustache 바인딩을 숨기는데 사용
- [v-cloak] { display : none } 와 같은 css 규칙과 함께 사용
- Vue 인스턴스가 준비되면 v-cloak는 제거됨

```jsx
[v-cloak] { display : none};

<div v-cloak>
  {{msg}}
</div>
```