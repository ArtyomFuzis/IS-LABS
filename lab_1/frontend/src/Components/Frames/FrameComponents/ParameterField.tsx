function ParameterField({required, type, field}: {required: boolean, type:string, field: string}){
    <div className="modal-parameter">
        <div className="modal-parameter-label">{field}
            {required && <span className="modal-parameter-obligatory-mark">*</span>}</div>
        {type == "text" && <input type="text" className="modal-parameter-input"></input>}
    </div>
}

export default ParameterField