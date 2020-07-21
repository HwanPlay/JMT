## Vue Method

- Vue 인스턴스는 생성 관련된 데이터 및 메서드의 정의가 가능
- 메서드 안에서 데이터를 this.데이터이름 으로 접근

```jsx
new Vue({
  el : "#app",
  data : {
    name : "홍길동"
  },
  methods : {
    enlnsa() {
      return "Hello" + this.name
    }
  }
});
```

## Vue filters

- filter를 이용하여 표현식에 새로운 결과 형식을 적용
- 중괄호보간법({{}}) 또는 v-bind 속성에서 사용이 가능
- vue 전역 필터

```jsx
Vue.filter(
  'count', (val) => {
    if(val.length == 0) {
      return;
    }
    return '${val} : ${val.length}자';
    }
};
```

- vue 지역 필터

```jsx
new Vue({
  el : '#app',
  filters : {
    count(val) {
      return '${val} : ${val.length}자';
    }
  }
});
```

## Vue computed 속성

- 특정 데이터의 변경사항을 실시간으로 처리할 수 있음
- 캐싱을 이용하여 데이터의 변경이 없을 경우 캐싱된 데이터를 반환
- setter와 getter를 직접 지정할 수 있음
- 작성은 메서드 형태로 하지만 vue에서 프록시 처리하여 프로퍼티처럼 사용

```jsx
var vm = new Vue({
  el : '#app',
  data : {
    message : '안녕하세요'
  },
  computed : {
    reversedMessage : function() {
      return this.message.splist('').reverse().join('')
    }
  }
});
```

## Vue watch 속성

- vue 인스턴스의 특정 프로퍼티가 변경될 때 실행할 콜백 함수 설정

```jsx
var vm = new Vue({
  el : '#app',
  data : {
    message : 'Hello',
    reversedMessage : '',
  },
  watch : {
    message : function (newVal, oldVal) {
      this.reversedMessage = newVal.split('').reverse().join('');
    }
  }
});

<div id = "app">
  <p> 원본 : "{{message}}"</p>
  <p> {{ reversedMessage }}"</p>
  <input type = "text" v-model = "message">
</div>
```

## Vue Event

- Dom 이벤트를 청취하기 위해 v-on 디렉티브 사용
- 인라인 이벤트 핸들링
- 메서드를 이용한 이벤트 핸들링
- event 청취 : v-on

```jsx
<div id = "app">
  <button v-on:click = "counter += 1">Add 1</button>
  <p> {{ counter }} 번 </p>
</div>

new Vue({
  el : '#app',
  data : {
    counter : 0
  }
});
```

- 메서드 이벤트 핸들러

```jsx
<div id="app">
  <button v-on:click="greet">Greet</button>
</div>

new Vue({
  el : '#app',
  data : {
    name : 'Vue.js'
  },
  methods : {
    greet : function(event) {
      alert('Hello ' + this.name + '!')
      console.dir(event.target);
    }
  }
});
```

- 인라인 메서드 핸들러

```jsx
<div id = "app">
  <button v-on:click="greet('VueJS')">Greet</button>
</div>

new Vue({
  methods: {
    greet : function(msg) {
      alert('Hello ' + msg + '!')
      console.dir(event.target);
    }
  }
});
```

## ref, $refs

- Vue 인스턴스 객체의 자식 컴포넌트 또는 DOM 엘리먼트 요소

```jsx
<div id = " app">
  아이디 : <input type = "text" v-model = "id" ref = "id">
</div>

new Vue({
  ..
  this.$refs.id
});
```

## class 바인딩

- 엘리먼트의 클래스와 스타일을 변경
- v-bind : class 는 조건에 따라서 클래스를 적용할 수 있음

```jsx
<div v-bind:class = "myclasses"></div>
new Vue({
  ...
  data : {
    myclasses : {'a' : true, 'b' : true, 'c' : false}
  }
});

<div v-bind : class = "{ 'a' : apply }"></div>

new Vue({
  ...
  data : {
    apply : true
  }
});
```

## 이벤트 수식어와 키 수식어

- v-on 이벤트의 수식어
    - .stop
    - .prevent
    - .once
- 키보드 이벤트 수식어
    - .13
    - .enter
    - .delete
- 수식어는 “.”으로 된 접미사를 말함
- v-on:click.stop, v-on:submit.prevent
- v-on:keyup.enter

## 폼 입력 바인딩

- v-model은 입력 요소에 대해서 특정 속성과 이벤트를 사용함
- text와 textarea : value, input 이벤트 사용
- checkbox, radio : checked, change 이벤트 사용
- select : value, change 이벤트 사용

## text, textarea

```jsx
<input v-model = "message" placeholder = "here">
<textarea v-model = "message" placeholder = "multiple line"></textarea>
<p>message : {{message}}</p>
```

## checkbox

- 하나의 체크박스일 경우 boolean 값을 표현

```jsx
<input type = "checkbox" id = "checkbox" v-model = "ck">
<label for = "checkbox">{{ck}}</label>
```

관리되는 값은 true 또는 false

- 여러개의 체크박스일 경우 배열의 형태로 값을 표현
- 배열의 값과 checkbox의 value 속성이 같을 경우 체크 처리됨

```jsx
<input type = "checkbox" value = "a" v-model = "cks"> A
<input type = "checkbox" value = "b" v-model = "cks"> B
new Vue({
  el : '#app',
  data : { cks : ['a'] }
})
```

## radio

- 라디오 박스일 경우 선택된 항목의 value 속성의 값을 관리

```jsx
<input type = "radio" id = "one" value = "One" v-model = "picked">
<label for ="one">One</label> <br>
<input type = "radio id = "two" value = "Two" v-model = "picked">
<label for = "tow">Two</label> <br>
<span>select : {{pickec}}</span>
```

## select

- 선택 박스일 경우 선택된 항목의 value 속성의 값을 관리

```jsx
<select v-model = "selectedHobby">
  <option value="">select</option>
  <option value="a">song</option>
  <option value="b">dance</option>
</select>

new Vue({
  el : "#app";
  date : {
    selectedHobby : ""
  }
});
```

---

# 컴포넌트

- Vue의 가장 강력한 기능 중 하나
- html 엘리먼트를 확장하여 재사용 가능한 코드를 캡슐화
- Vue 인스턴스의 옵션을 대부분 사용
- 라이프사이클 훅 사용 가능
- 전역 컴포넌트와 지역 컴포넌트

## 전역 컴포넌트 등록

- 권장하는 컴포넌트 이름 : 케밥 표기법(소문자, -)

```jsx
Vue.component(tagName, options)

Vue.component("my-comp", {
  //option
});
```

```jsx
<div id = "app1">
  <my-comp></my-comp>
</div>

<div id = "app2">
  <my-comp></my-comp>
</div>

Vue.component('my-comp', {
  template : '<div>static component</div>'
});
new Vue({
  el : '#app1'
});
new Vue({
  el : '#app2'
});
```

## 지역 컴포넌트 등록

```jsx
<div id = "app1">
  <my-comp></my-comp>
</div>

let comp = {
  template : '<div>local component</div>'
}

new Vue({
  el : "#app1",
  components : {
    'my-comp' : comp
  }
});
```

## 컴포넌트 템플릿

```jsx
<div id = "app">
  <my-comp></my-comp>
</div>

<template id = "my-temp">
  <div>local</div>
</template>
Vue.component('my-comp', {
  template : "#my-temp"
})
new Vue({
  el : '#app'
})
```

## 컴포넌트 data 공유

```jsx
<div id = "app>
  <count-view></count-view>
  <count-view></count-view>
</div>
<template id = "count-view">
<div>
  <span>{{count}}</span>
  <button @click = "count++">..</button>
</div>
</template>

let data = { count : 0 };
Vue.component('count-view', {
  data() {
    return data
  },
  template : '#count-view'
});
```

## 컴포넌트 data 함수

```jsx
<div id = "app>
   <count-view></count-view>
   <count-view></count-view>
</div>
<template id = "count-view">
<div>
   <span>{{count}}</span>
   <button @click = "count++">..</button>
</div>
</temtplate>
 
Vue.component('count-view', {
   data() {
     return {
      count : 0
    }
  },
  template : '#count-view'
})
```