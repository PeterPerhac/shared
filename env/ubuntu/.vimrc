let vimDir = '$HOME/.vim'
let &runtimepath.=','.vimDir

" Keep undo history across sessions by storing it in a file
if has('persistent_undo')
    let myUndoDir = expand(vimDir . '/undodir')
    " Create dirs
    call system('mkdir ' . vimDir)
    call system('mkdir ' . myUndoDir)
    let &undodir = myUndoDir
    set undofile
endif

set number
set tabstop=8 softtabstop=0 expandtab shiftwidth=4 smarttab
syntax on
colorscheme koehler

noremap <Space> <PageDown>
noremap <BS> <PageUp>

