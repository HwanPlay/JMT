<template>
  <div>
    <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
      <div class="menubar">
        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bold() }"
          @click="commands.bold"
        >
          <b-icon-type-bold v-b-tooltip.hover title="Bold" class="h3 mb-2 border rounded"></b-icon-type-bold>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.italic() }"
          @click="commands.italic"
        >
          <b-icon-type-italic v-b-tooltip.hover title="Italic" class="h3 mb-2 border rounded"></b-icon-type-italic>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.strike() }"
          @click="commands.strike"
        >
          <b-icon-type-strikethrough
            v-b-tooltip.hover
            title="Strike"
            class="h3 mb-2 border rounded"
          ></b-icon-type-strikethrough>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.underline() }"
          @click="commands.underline"
        >
          <b-icon-type-underline
            v-b-tooltip.hover
            title="Under line"
            class="h3 mb-2 border rounded"
          ></b-icon-type-underline>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code() }"
          @click="commands.code"
        >
          <b-icon-code v-b-tooltip.hover title="Code" class="h3 mb-2 border rounded"></b-icon-code>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.paragraph() }"
          @click="commands.paragraph"
        >
          <b-icon-pencil v-b-tooltip.hover title="Paragraph" class="h3 mb-2 border rounded"></b-icon-pencil>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 1 }) }"
          @click="commands.heading({ level: 1 })"
        >
          <b-icon-type-H1 v-b-tooltip.hover title="Heading 1" class="h3 mb-2 border rounded"></b-icon-type-H1>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 2 }) }"
          @click="commands.heading({ level: 2 })"
        >
          <b-icon-type-H2 v-b-tooltip.hover title="Heading 2" class="h3 mb-2 border rounded"></b-icon-type-H2>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 3 }) }"
          @click="commands.heading({ level: 3 })"
        >
          <b-icon-type-H3 v-b-tooltip.hover title="Heading 3" class="h3 mb-2 border rounded"></b-icon-type-H3>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bullet_list() }"
          @click="commands.bullet_list"
        >
          <b-icon-list-ul v-b-tooltip.hover title="Unordered List" class="h3 mb-2 border rounded"></b-icon-list-ul>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.ordered_list() }"
          @click="commands.ordered_list"
        >
          <b-icon-list-ol v-b-tooltip.hover title="Ordered List" class="h3 mb-2 border rounded"></b-icon-list-ol>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.blockquote() }"
          @click="commands.blockquote"
        >
          <b-icon-blockquote-left
            v-b-tooltip.hover
            title="Block Quote"
            class="h3 mb-2 border rounded"
          ></b-icon-blockquote-left>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code_block() }"
          @click="commands.code_block"
        >
          <b-icon-file-code v-b-tooltip.hover title="Code Block" class="h3 mb-2 border rounded"></b-icon-file-code>
        </button>

        <button class="menubar__button" @click="commands.horizontal_rule">
          <b-icon-hr v-b-tooltip.hover title="Br" class="h3 mb-2 border rounded"></b-icon-hr>
        </button>

        <button class="menubar__button" @click="commands.undo">
          <b-icon-arrow90deg-left v-b-tooltip.hover title="Undo" class="h3 mb-2 border rounded"></b-icon-arrow90deg-left>
        </button>

        <button class="menubar__button" @click="commands.redo">
          <b-icon-arrow90deg-right v-b-tooltip.hover title="Redo" class="h3 mb-2 border rounded"></b-icon-arrow90deg-right>
        </button>
      </div>
    </editor-menu-bar>
  </div>
</template>

<script>
export default {};
</script>

<style>
</style>