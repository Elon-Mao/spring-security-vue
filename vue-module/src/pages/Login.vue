<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import type { FormInstance, FormRules } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const user = useUserStore()

interface RuleForm {
  username: string
  password: string
}

const ruleFormRef = ref<FormInstance>()
const ruleForm = reactive<RuleForm>({
  username: '',
  password: ''
})

const rules = reactive<FormRules<RuleForm>>({
  username: [
    { required: true, message: 'Please input username', trigger: 'blur' },
    { min: 4, max: 16, message: 'Length should be 4 to 16', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please input password', trigger: 'blur' },
    { min: 4, max: 16, message: 'Length should be 4 to 16', trigger: 'blur' }
  ]
})

function objectToFormData(obj: Record<string, any>): FormData {
  const formData = new FormData()
  for (const key in obj) {
    if (obj.hasOwnProperty(key)) {
      formData.append(key, obj[key])
    }
  }
  return formData
}

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) {
    return
  }
  await formEl.validate(async valid => {
    if (!valid) {
      return
    }
    const loginResponse = await fetch('/api/login', {
      method: 'POST',
      body: objectToFormData(ruleForm)
    })
    if (loginResponse.status === 200) {
      const userResponse = await fetch(`/api/persons/${ruleForm.username}`)
      user.setUser(await userResponse.json())
      router.push('/')
    }
  })
}
</script>

<template>
  <el-form ref="ruleFormRef" :model="ruleForm" :rules="rules" label-width="120px" class="demo-ruleForm" status-icon>
    <el-form-item label="Username" prop="username">
      <el-input v-model="ruleForm.username" />
    </el-form-item>
    <el-form-item label="Password" prop="password">
      <el-input v-model="ruleForm.password" type="password" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm(ruleFormRef)">
        Login
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped></style>
