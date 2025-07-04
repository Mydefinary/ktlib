<template>
  <div class="scroll-wrapper">
    <!-- Author ID 입력 (일반 필드) -->
    <v-text-field
      label="Author ID"
      v-model.number="value.authorId"
      type="number"
      outlined
      dense
      class="mb-4"
    />
 
    <!-- 제목 -->
    <v-text-field
      label="Title"
      v-model="value.title"
      outlined
      dense
      class="mb-4"
    />
 
    <!-- 내용 -->
    <v-textarea
      label="Content"
      v-model="value.content"
      auto-grow
      rows="8"
      outlined
      dense
      class="mb-4"
    />
 
    <!-- 저장 버튼 -->
    <v-row class="pt-4">
      <v-spacer></v-spacer>
      <v-btn
        color="primary"
        @click="save"
      >
        저장
      </v-btn>
    </v-row>
  </div>
</template>
 
<script>
import BaseEntity from './base-ui/BaseEntity.vue'
 
export default {
  name: 'Manuscript',
  mixins: [BaseEntity],
  props: {
    modelValue: Object
  },
  data: () => ({
    path: 'manuscripts',
    value: {
      title: '',
      content: '',
      authorId: null,
      authorNickname: '',
      createdDate: null,
      lastModified: null,
      status: 'DRAFT'
    }
  }),
  methods: {
    async save() {
      if (!this.value.createdDate) {
        this.value.createdDate = new Date().toISOString();
      }
      this.value.lastModified = new Date().toISOString();
 
      try {
        const result = await this.$options.mixins[0].methods.save.call(this);
        console.log('✅ 저장 완료:', result);
        this.$emit('add', this.value);
      } catch (e) {
        console.error('❌ 저장 실패:', e);
      }
    }
  }
}
</script>
 
<style scoped>
.scroll-wrapper {
  max-height: 80vh;
  overflow-y: auto;
  padding: 24px;
}
</style>
 
 