// theme.ts
import '@emotion/react'

export type CustomTheme = {
  color: {
    primary: string
    secondary: string
    modify: string
    text: {
      regular: string
      low: string
      disabled: string
      distructive: string
      confirm: string
      saturday: string
      sunday: string
    }
    background: {
      update: string
      delete: string
      create: string
      white: string
    }
    border: string
  }
  typography: {
    fonts: {
      paperlogyRegular: string
      paperlogyMedium: string
      paperlogySemiBold: string
      paperlogyBold: string
    }
    styles: {
      topHeader: {
        fontFamily: string
        fontSize: string
      }
      title: {
        fontFamily: string
        fontSize: string
      }
      default: {
        fontFamily: string
        fontSize: string
      }
      name: {
        fontFamily: string
        fontSize: string
      }
      inputBoxTitle: {
        fontFamily: string
        fontSize: string
      }
      description: {
        fontFamily: string
        fontSize: string
      }
      button: {
        fontFamily: string
        fontSize: string
      }
      heading: {
        fontFamily: string
        fontSize: string
      }
      navigator: {
        fontFamily: string
        fontSize: string
      }
      tiny: {
        fontFamily: string
        fontSize: string
      }
      tinyBold: {
        fontFamily: string
        fontSize: string
      }
      descriptionBold: {
        fontFamily: string
        fontSize: string
      }
      defaultHighlight: {
        fontFamily: string
        fontSize: string
      }
      cardDescription: {
        fontFamily: string
        fontSize: string
      }
      accountAndCardNum: {
        fontFamily: string
        fontSize: string
      }
    }
  }
}

declare module '@emotion/react' {
  export interface Theme extends CustomTheme {}
}

const theme: CustomTheme = {
  color: {
    primary: '#54a0ff',
    secondary: '#ebeef2',
    modify: '#fec959',
    text: {
      regular: '#292f35',
      low: '#586575',
      disabled: '#A4B6CC',
      distructive: '#f96e69',
      confirm: '#2dbd99',
      saturday: '#74bcff',
      sunday: '#ff8c8f',
    },
    background: {
      update: '#fffbe8',
      delete: '#ffe3e5',
      create: '#d4ffdb',
      white: '#ffffff',
    },
    border: '#d9d6e7',
  },
  typography: {
    fonts: {
      paperlogyRegular: 'var(--font-paperlogy-regular)',
      paperlogyMedium: 'var(--font-paperlogy-medium)',
      paperlogySemiBold: 'var(--font-paperlogy-semi-bold)',
      paperlogyBold: 'var(--font-paperlogy-bold)',
    },
    styles: {
      topHeader: {
        fontFamily: 'var(--font-paperlogy-medium)',
        fontSize: '1.125rem',
      },
      title: {
        fontFamily: 'var(--font-paperlogy-semi-bold)',
        fontSize: '22px',
      },
      default: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '1.125rem',
      },
      name: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '0.875rem',
      },
      inputBoxTitle: {
        fontFamily: 'var(--font-paperlogy-medium)',
        fontSize: '0.875rem',
      },
      description: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '0.875rem',
      },
      button: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '1.25rem',
      },
      heading: {
        fontFamily: 'var(--font-paperlogy-semi-bold)',
        fontSize: '22px',
      },
      navigator: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '0.625rem',
      },
      tiny: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '0.5rem',
      },
      tinyBold: {
        fontFamily: 'var(--font-paperlogy-bold)',
        fontSize: '0.5rem',
      },
      descriptionBold: {
        fontFamily: 'var(--font-paperlogy-semi-bold)',
        fontSize: '0.875rem',
      },
      defaultHighlight: {
        fontFamily: 'var(--font-paperlogy-bold)',
        fontSize: '1.125rem',
      },
      cardDescription: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '0.75rem',
      },
      accountAndCardNum: {
        fontFamily: 'var(--font-paperlogy-regular)',
        fontSize: '1rem',
      },
    },
  },
}

export default theme
