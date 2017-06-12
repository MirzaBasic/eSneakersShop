using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace eSneakers.MVC.Models
{
    public class KomentarVM
    {
        public int Id { get; set; }

   

        public String Korisnik { get; set; }
      
      
        public int ProizvodId { get; set; }
        public String DatumKreiranja { get; set; }

        public String Sadrzaj { get; set; }
    }
}