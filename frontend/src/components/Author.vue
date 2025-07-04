<template>
    <div>
        <div>
        <String
            label="이름"
            v-model="value.authorName"
            :editMode="editMode"
            class="mb-2"
        />
        <String
            label="닉네임"
            v-model="value.authorNickname"
            :editMode="editMode"
            class="mb-2"
        />
        <String
            label="연락처"
            v-model="value.phoneNumber"
            :editMode="editMode"
            class="mb-2"
        />
        <String
            label="이메일"
            v-model="value.email"
            :editMode="editMode"
            class="mb-2"
        />
        <String
            label="포트폴리오 URL"
            v-model="value.portfolioUrl"
            :editMode="editMode"
            class="mb-2"
        />
        </div>
        <div class="mb-2">* 등록 신청 시 수정이 불가능합니다</div>
        <v-row class="ma-0 pa-0">
            <v-spacer></v-spacer>
            <v-btn width="64px" color="primary" @click="save">
                저장
            </v-btn>
        </v-row>
    </div>
</template>


<script>
import BaseEntity from './base-ui/BaseEntity.vue'
// axios를 import 합니다.
import axios from 'axios';

export default {
    name: 'Author',
    mixins:[BaseEntity],
    components:{
    },
    
    data: () => ({
        path: "authors",
        value: {
            authorName: '',
            authorNickname: '',
            phoneNumber: '',
            email: '',
            portfolioUrl: '',
        }
    }),
    created(){
    },
    methods: {
        // BaseEntity의 save 메소드를 덮어쓰는 새로운 save 메소드를 만듭니다.
        async save() {
            try {
                // 1. API 경로를 '/authors/register'로 지정합니다.
                const url = `/${this.path}/register`;
                
                // 2. 데이터를 POST 방식으로 보냅니다.
                const response = await axios.post(url, this.value);
                
                console.log("작가 등록 성공:", response.data);
                
                // 성공 후 창을 닫거나 목록을 새로고침하는 로직을 추가할 수 있습니다.
                this.$emit('close');

            } catch (error) {
                console.error("작가 등록 실패:", error);
                // 사용자에게 오류 메시지를 보여줍니다.
                alert(`작가 등록에 실패했습니다. 오류: ${error.response ? error.response.status : error.message}`);
            }
        }
    },
}
</script>
