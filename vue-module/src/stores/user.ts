import { defineStore } from 'pinia'
import User from '@/types/User'

export const useUserStore = defineStore('user', {
  state: () => {
    for (const cookie of document.cookie.split(';')) {
      const [cookieName, cookieValue] = cookie.trim().split('=')
      if (cookieName === 'user') {
        return {
          user: JSON.parse(cookieValue) as User
        }
      }
    }
    return {
      user: null
    }
  },
  actions: {
    setUser(user: User) {
      this.user = user
      const date = new Date()
      date.setTime(date.getTime() + (4 * 24 * 60 * 60 * 1000))
      const expires = "expires=" + date.toUTCString()
      document.cookie = "user=" + JSON.stringify(user) + "; " + expires
    },
    deleteUser() {
      document.cookie = 'user=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    }
  },
})