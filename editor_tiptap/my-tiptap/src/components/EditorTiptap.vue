<template>
  <div class="editor">
    <editor-menu-bar :editor="editor" v-slot="{ commands, isActive }">
      <div class="menubar">

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bold() }"
          @click="commands.bold"
        >
        <b-icon-type-bold class="h3 mb-2 border rounded"></b-icon-type-bold>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.italic() }"
          @click="commands.italic"
        >
        <b-icon-type-italic class="h3 mb-2 border rounded"></b-icon-type-italic>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.strike() }"
          @click="commands.strike"
        >
        <b-icon-type-strikethrough class="h3 mb-2 border rounded"></b-icon-type-strikethrough>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.underline() }"
          @click="commands.underline"
        >
        <b-icon-type-underline class="h3 mb-2 border rounded"></b-icon-type-underline>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code() }"
          @click="commands.code"
        >
        <b-icon-code class="h3 mb-2 border rounded"></b-icon-code>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.paragraph() }"
          @click="commands.paragraph"
        >
        <b-icon-pencil class="h3 mb-2 border rounded"></b-icon-pencil>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 1 }) }"
          @click="commands.heading({ level: 1 })"
        >
        <b-icon-type-H1 class="h3 mb-2 border rounded"></b-icon-type-H1>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 2 }) }"
          @click="commands.heading({ level: 2 })"
        >
        <b-icon-type-H2 class="h3 mb-2 border rounded"></b-icon-type-H2>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.heading({ level: 3 }) }"
          @click="commands.heading({ level: 3 })"
        >
        <b-icon-type-H3 class="h3 mb-2 border rounded"></b-icon-type-H3>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.bullet_list() }"
          @click="commands.bullet_list"
        >
        <b-icon-list-ul class="h3 mb-2 border rounded"></b-icon-list-ul>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.ordered_list() }"
          @click="commands.ordered_list"
        >
        <b-icon-list-ol class="h3 mb-2 border rounded"></b-icon-list-ol>
        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.blockquote() }"
          @click="commands.blockquote"
        >
        <b-icon-blockquote-left class="h3 mb-2 border rounded"></b-icon-blockquote-left>

        </button>

        <button
          class="menubar__button"
          :class="{ 'is-active': isActive.code_block() }"
          @click="commands.code_block"
        >
        <b-icon-file-code class="h3 mb-2 border rounded"></b-icon-file-code>
        </button>

        <button
          class="menubar__button"
          @click="commands.horizontal_rule"
        >
        <b-icon-hr class="h3 mb-2 border rounded"></b-icon-hr>
        </button>

        <button
          class="menubar__button"
          @click="commands.undo"
        >
        <b-icon-arrow90deg-left class="h3 mb-2 border rounded"></b-icon-arrow90deg-left>
        </button>

        <button
          class="menubar__button"
          @click="commands.redo"
        >        
        <b-icon-arrow90deg-right class="h3 mb-2 border rounded"></b-icon-arrow90deg-right>
        </button>

      </div>
    </editor-menu-bar>

    <editor-content class="editor__content" :editor="editor" />
  </div>
</template>

<script>
import { Editor, EditorContent, EditorMenuBar } from 'tiptap'
import {
  Blockquote,
  CodeBlock,
  HardBreak,
  Heading,
  HorizontalRule,
  OrderedList,
  BulletList,
  ListItem,
  TodoItem,
  TodoList,
  Bold,
  Code,
  Italic,
  Link,
  Strike,
  Underline,
  History,
} from 'tiptap-extensions'
export default {
  components: {
    EditorContent,
    EditorMenuBar,
  },
  data() {
    return {
      editor: new Editor({
        extensions: [
          new Blockquote(),
          new BulletList(),
          new CodeBlock(),
          new HardBreak(),
          new Heading({ levels: [1, 2, 3] }),
          new HorizontalRule(),
          new ListItem(),
          new OrderedList(),
          new TodoItem(),
          new TodoList(),
          new Link(),
          new Bold(),
          new Code(),
          new Italic(),
          new Strike(),
          new Underline(),
          new History(),
        ],
        content: `
          <h2>
            Hi there,
          </h2>
          <p>
            this is a very <em>basic</em> example of tiptap.
          </p>
          <pre><code>body { display: none; }</code></pre>
          <ul>
            <li>
              A regular list
            </li>
            <li>
              With regular items
            </li>
          </ul>
          <blockquote>
            It's amazing 👏
            <br />
            – mom
          </blockquote>
        `,
      }),
    }
  },
  beforeDestroy() {
    this.editor.destroy()
  },
}
</script>

<style scoped>

</style>